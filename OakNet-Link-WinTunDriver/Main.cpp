#define _WINSOCKAPI_
#include "Main.h"
#include "Config.h"
#include "wintun.h"
#include <iostream>
#include <winsock2.h>
#include <Windows.h>
#include <ws2ipdef.h>
#include <iphlpapi.h>
#include <mstcpip.h>
#include <ip2string.h>
#include <winternl.h>
#include <stdarg.h>
#include <stdlib.h>
#include <sstream>
#include <vector>
#include <tuple>
#include <iomanip>
#include <mutex>
#pragma comment(lib, "rpcrt4.lib")

static WINTUN_CREATE_ADAPTER_FUNC* WintunCreateAdapter;
static WINTUN_CLOSE_ADAPTER_FUNC* WintunCloseAdapter;
static WINTUN_OPEN_ADAPTER_FUNC* WintunOpenAdapter;
static WINTUN_GET_ADAPTER_LUID_FUNC* WintunGetAdapterLUID;
static WINTUN_GET_RUNNING_DRIVER_VERSION_FUNC* WintunGetRunningDriverVersion;
static WINTUN_DELETE_DRIVER_FUNC* WintunDeleteDriver;
static WINTUN_SET_LOGGER_FUNC* WintunSetLogger;
static WINTUN_START_SESSION_FUNC* WintunStartSession;
static WINTUN_END_SESSION_FUNC* WintunEndSession;
static WINTUN_GET_READ_WAIT_EVENT_FUNC* WintunGetReadWaitEvent;
static WINTUN_RECEIVE_PACKET_FUNC* WintunReceivePacket;
static WINTUN_RELEASE_RECEIVE_PACKET_FUNC* WintunReleaseReceivePacket;
static WINTUN_ALLOCATE_SEND_PACKET_FUNC* WintunAllocateSendPacket;
static WINTUN_SEND_PACKET_FUNC* WintunSendPacket;

template <typename T>
std::string Str(const T& t) {
    std::ostringstream os;
    os << t;
    return os.str();
}

LogCallback logger;

void logInfo(std::string message) {
    if(&logger)
        logger((std::string("[I]: ") + message).c_str());
}

void logError(std::string message) {
    if(&logger)
        logger((std::string("[E]: ") + message).c_str());
}

static HMODULE InitializeWintun(void)
{
    HMODULE Wintun =
        LoadLibraryExW(L"wintun.dll", NULL, LOAD_LIBRARY_SEARCH_APPLICATION_DIR | LOAD_LIBRARY_SEARCH_SYSTEM32);
    if (!Wintun)
        return NULL;
#define X(Name) ((*(FARPROC *)&Name = GetProcAddress(Wintun, #Name)) == NULL)
    if (X(WintunCreateAdapter) || X(WintunCloseAdapter) || X(WintunOpenAdapter) || X(WintunGetAdapterLUID) ||
        X(WintunGetRunningDriverVersion) || X(WintunDeleteDriver) || X(WintunSetLogger) || X(WintunStartSession) ||
        X(WintunEndSession) || X(WintunGetReadWaitEvent) || X(WintunReceivePacket) || X(WintunReleaseReceivePacket) ||
        X(WintunAllocateSendPacket) || X(WintunSendPacket))
#undef X
    {
        DWORD LastError = GetLastError();
        FreeLibrary(Wintun);
        SetLastError(LastError);
        logError(std::string("Error whilst loading the lib: ") + Str(LastError));
        return NULL;
    }
    return Wintun;
}

static HANDLE QuitEvent;
static volatile BOOL HaveQuit;
static ProgressCallback callBack;

static DWORD WINAPI ReceivePackets(_Inout_ DWORD_PTR SessionPtr)
{
    WINTUN_SESSION_HANDLE Session = (WINTUN_SESSION_HANDLE)SessionPtr;
    HANDLE WaitHandles[] = { WintunGetReadWaitEvent(Session), QuitEvent };

    while (true)
    {
        DWORD PacketSize;
        BYTE* Packet = WintunReceivePacket(Session, &PacketSize);
        if (Packet)
        {
            //logger::log(std::string("Received packet with size: ") + Str(PacketSize));
            callBack(Packet, PacketSize);
            WintunReleaseReceivePacket(Session, Packet);
        }
        else
        {
            DWORD LastError = GetLastError();
            switch (LastError)
            {
            case ERROR_NO_MORE_ITEMS:
                if (WaitForMultipleObjects(_countof(WaitHandles), WaitHandles, FALSE, INFINITE) == WAIT_OBJECT_0)
                    continue;
                return ERROR_SUCCESS;
            default:
                logError("Packet read failed");
                return LastError;
            }
        }
    }
    return ERROR_SUCCESS;
}

std::vector<std::tuple<byte*, int*>*> queue;
std::mutex mylock;

void sendPacket(byte* packetData, int length) {
    auto size = new int;
    *size = length;
    auto packetData_ = new byte[*size];
    memcpy(packetData_, packetData, *size);
    mylock.lock();
    queue.push_back(new std::tuple<byte*, int*>(packetData_, size));
    mylock.unlock();
}

static DWORD WINAPI
SendPackets(_Inout_ DWORD_PTR SessionPtr)
{
    WINTUN_SESSION_HANDLE Session = (WINTUN_SESSION_HANDLE)SessionPtr;
    while (!HaveQuit)
    {
        while (!queue.empty()) {
            mylock.lock();
            auto toSend = queue.front();
            mylock.unlock();
            auto length = std::get<1>(*toSend);
            auto packetData = std::get<0>(*toSend);

            BYTE* Packet = WintunAllocateSendPacket(Session, *length);
            if (Packet)
            {
                memcpy(Packet, packetData, *length);
                WintunSendPacket(Session, Packet);
                mylock.lock();
                queue.erase(queue.begin());
                mylock.unlock();

                //logger::log("Packet sent!");

                delete[] packetData;
                delete length;
                delete toSend;
            }
            else if (GetLastError() != ERROR_BUFFER_OVERFLOW) {
                logError("Buffer Overflow");
                break;
            }
        }

        switch (WaitForSingleObject(QuitEvent, 1 /* 1 ms */))
        {
        case WAIT_ABANDONED:
        case WAIT_OBJECT_0:
            return ERROR_SUCCESS;
        }
    }
    return ERROR_SUCCESS;
}

void stopTunDevice() {
}

int startTunDevice(uint32_t ipv4Adress, ProgressCallback progressCallback, LogCallback logCallback)
{
    logger = logCallback;
	logInfo(std::string("Starting OakNet-Link Tunnel VERSION: ") + Str(OakNet_Link_Tunnel_VERSION_MAJOR) + std::string(".") + Str(OakNet_Link_Tunnel_VERSION_MINOR));
    HMODULE Wintun = InitializeWintun();
    if (!Wintun) {
        logError("Failed to initialize Wintun");
        FreeLibrary(Wintun);
        return 1;
    }
    //WintunSetLogger(ConsoleLogger);
    logInfo("Wintun library loaded");
    callBack = progressCallback;
    //HaveQuit = FALSE;
    QuitEvent = CreateEventW(NULL, TRUE, FALSE, NULL);
    if (!QuitEvent)
    {
        logError("Failed to create event");
        FreeLibrary(Wintun);
    }
    //if (!SetConsoleCtrlHandler(CtrlHandler, TRUE))
    //{
    //    logger::logError("Failed to set console handler");
    //    goto cleanupQuit;
    //}

    UUID newId;
    UuidCreate(&newId);
    WINTUN_ADAPTER_HANDLE Adapter = WintunCreateAdapter(L"OakNet-Link", L"OakNet-Link", &newId);
    if (!Adapter)
    {
        logError("Failed to create adapter");
        //SetConsoleCtrlHandler(CtrlHandler, FALSE);
        CloseHandle(QuitEvent);
        FreeLibrary(Wintun);
        return 10;
    }

    DWORD Version = WintunGetRunningDriverVersion();
    logInfo(std::string("Wintun v") + Str((Version >> 16) & 0xff) + std::string(".") + Str((Version >> 0) & 0xff) + std::string(" loaded"));

    MIB_UNICASTIPADDRESS_ROW AddressRow;
    InitializeUnicastIpAddressEntry(&AddressRow);
    WintunGetAdapterLUID(Adapter, &AddressRow.InterfaceLuid);
    AddressRow.Address.Ipv4.sin_family = AF_INET;
    AddressRow.Address.Ipv4.sin_addr.S_un.S_addr = htonl(ipv4Adress); 
    AddressRow.OnLinkPrefixLength = 16; /* This is a /16 network */
    AddressRow.DadState = IpDadStatePreferred;
    auto LastError = CreateUnicastIpAddressEntry(&AddressRow);
    if (LastError != ERROR_SUCCESS && LastError != ERROR_OBJECT_ALREADY_EXISTS)
    {
        logError("Failed to set IP address");
        WintunCloseAdapter(Adapter);
        //SetConsoleCtrlHandler(CtrlHandler, FALSE);
        CloseHandle(QuitEvent);
        FreeLibrary(Wintun);
        return 11;
    }

    WINTUN_SESSION_HANDLE Session = WintunStartSession(Adapter, 0x400000);
    if (!Session)
    {
        logError("Failed to create adapter session"); 
        WintunCloseAdapter(Adapter);
        CloseHandle(QuitEvent);
        FreeLibrary(Wintun);
        return 12;
    }

    logInfo("Launching threads and mangling packets...");

    HANDLE Workers[] = { CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)ReceivePackets, (LPVOID)Session, 0, NULL),
                         CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)SendPackets, (LPVOID)Session, 0, NULL) };
    if (!Workers[0] || !Workers[1])
    {
        logError("Failed to create threads");
        HaveQuit = TRUE;
        SetEvent(QuitEvent);
        for (size_t i = 0; i < _countof(Workers); ++i)
        {
            if (Workers[i])
            {
                WaitForSingleObject(Workers[i], INFINITE);
                CloseHandle(Workers[i]);
            }
        }
        WintunEndSession(Session);
        WintunCloseAdapter(Adapter);
        //SetConsoleCtrlHandler(CtrlHandler, FALSE);
        CloseHandle(QuitEvent);
        FreeLibrary(Wintun);
        return 13;
    }
    WaitForMultipleObjectsEx(_countof(Workers), Workers, TRUE, INFINITE, TRUE);
    LastError = ERROR_SUCCESS;
    HaveQuit = TRUE;
    SetEvent(QuitEvent);
    for (size_t i = 0; i < _countof(Workers); ++i)
    {
        if (Workers[i])
        {
            WaitForSingleObject(Workers[i], INFINITE);
            CloseHandle(Workers[i]);
        }
    }
    WintunEndSession(Session);
    WintunCloseAdapter(Adapter);
    //SetConsoleCtrlHandler(CtrlHandler, FALSE);
    CloseHandle(QuitEvent);
    FreeLibrary(Wintun);
    return 0;
}
