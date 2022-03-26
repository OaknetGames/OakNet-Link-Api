// OakNet-Link-Tunnel.h: Includedatei für Include-Standardsystemdateien
// oder projektspezifische Includedateien.

#pragma once
#include "Windows.h"
#include <cstdint>
#include <string>

#ifdef __cplusplus
extern "C"
{
#endif

#define DLL __declspec(dllexport)

    typedef void(__stdcall* ProgressCallback)(byte*, int size);

    typedef void(__stdcall* LogCallback)(LPCSTR s);

    DLL int startTunDevice(uint32_t ipv4Adress, ProgressCallback progressCallback, LogCallback logCallback);

    DLL void sendPacket(byte* packetData, int length);

    DLL void stopTunDevice();

#ifdef __cplusplus
}
#endif

void logInfo(std::string message);
void logError(std::string message);

