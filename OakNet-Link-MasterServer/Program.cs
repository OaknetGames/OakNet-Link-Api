using OakNetLink.Api;
using OakNetLink.Api.Communication;
using OakNetLink.Tunnel;
using System;

namespace OakNetLink.MasterServer
{
    public static class Program
    {
        public static void Main()
        {
            //Wire Events:
#pragma warning disable CS8602 // Dereferenzierung eines möglichen Nullverweises.
            ONL.Event.Log += (obj, args) => Console.WriteLine((args as ONL.Event.LogEventArgs).message);
            ONL.Event.ConnectionFailed += (obj, args) => Console.WriteLine("ConnectionFailed!");
            ONL.Event.Disconnection += (obj, args) => Console.WriteLine((args as ONL.Event.DisconnectEventArgs).endpoint.IpAddress.ToString() + ":" + (args as ONL.Event.DisconnectEventArgs).endpoint.Port + " disconnected: " + (args as ONL.Event.DisconnectEventArgs).reason);
            ONL.Event.ConnectionEstablished += (obj, args) => Console.WriteLine((obj as OakNetEndPoint).IpAddress + ":" + (obj as OakNetEndPoint).Port + " Connected!");
            ONL.Event.ConnectionLost += (obj, args) => Console.WriteLine("LostConnection!");
            ONL.Event.ConnectionRequest += (obj, args) => { Console.WriteLine((obj as OakNetEndPoint).IpAddress + " requested Connection"); (args as ONL.Event.ConnectionRequestEventArgs).accepted = true; };
#pragma warning restore CS8602 // Dereferenzierung eines möglichen Nullverweises.

            //Register ONL Plugins
            ONL.registerPlugin(new TunnelPlugin());

            //Configure TunnelPlugin
            TunnelPlugin.Manager = new AddressManager();

            //Start the Server
            ONL.MasterServer.StartServer();

            Console.WriteLine("Press any key to quit");
            Console.ReadKey();
        }
    }
}
