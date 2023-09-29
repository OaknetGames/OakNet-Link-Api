using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel
{
    internal class TunDriverWrapper
    {
        [UnmanagedFunctionPointer(CallingConvention.StdCall)]
        delegate void ProgressCallback(IntPtr data, int length);

        [UnmanagedFunctionPointer(CallingConvention.StdCall)]
        delegate void LogCallback(string data);


        [DllImport("OakNet-Link-WinTunDriver.dll", CallingConvention = CallingConvention.Cdecl, CharSet = CharSet.Ansi)]
        static extern int startTunDevice(uint ipAddress, [MarshalAs(UnmanagedType.FunctionPtr)] ProgressCallback callbackPointer, LogCallback logCallback);

        [DllImport("OakNet-Link-WinTunDriver.dll", CallingConvention = CallingConvention.Cdecl, CharSet = CharSet.Ansi)]
        static extern void sendPacket([In,Out][MarshalAs(UnmanagedType.LPArray)] byte[] packetData, int length);

        static bool initialized = false;
        static Action<byte[]> onPacketReceived;

        public static int CurrentAddress { get; private set; }

        public static void createAdapter(int ipAddress)
        {
            CurrentAddress = ipAddress;
            if (initialized)
                return;
            initialized = true;
            try
            {
                OakNetLink.Api.Logger.log("Creating TunAdapter on: " + ipAddress);
                ProgressCallback callback = (data, length) => {
                    var arr = new byte[length];
                    Marshal.Copy(data, arr, 0, length);
                    //Console.Write($"Received Packet: Source: {arr[12]}.{arr[13]}.{arr[14]}.{arr[15]} \t");
                    //Console.WriteLine($"Dest: {arr[16]}.{arr[17]}.{arr[18]}.{arr[19]}");
                    if (onPacketReceived != null)
                        onPacketReceived(arr);
                };

                LogCallback logCallback = (s) => {
                    OakNetLink.Api.Logger.log("WinTunDriver: " + s);
                };

                Task.Factory.StartNew(() => {
                    OakNetLink.Api.Logger.log("Blep");
                    int error = startTunDevice((uint)ipAddress, callback, logCallback);
                    OakNetLink.Api.Logger.log("Blop");
                    initialized = false;
                    if (error != 0)
                        throw new Exception("Can't create TUN adapter code: " + error);
                });
            }catch(Exception e)
            {
                OakNetLink.Api.Logger.log(e.Message);
            }
            
        }

        public static void deleteAdapter()
        {
            if(!initialized)
                return ;

            initialized = false;
        }

        public static void setOnPacketReceived(Action<byte[]> action)
        {
            onPacketReceived = action;
        }

        public static void sendPacketData(byte[] packetData)
        {
            sendPacket(packetData, packetData.Length);
        }
    }
}
