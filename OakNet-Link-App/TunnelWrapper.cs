using OakNetLink.Api;
using OakNetLink.Api.Communication;
using OakNetLink.Tunnel;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace OakNetLink.App
{
    internal class TunnelWrapper
    {
        public static List<string> log = new List<string>();

        public static void prepare()
        {
            var writer = new StringWriter();
            Console.SetOut(writer);

            //TunDriverWrapper.createAdapter((10 << 24) | (6 << 16) | (0 << 8) | 4);
            // Register all the EventHandler
            ONL.Event.Log += (obj, args) => 
            {
                Console.WriteLine((args as ONL.Event.LogEventArgs).message);
                string time = DateTime.Now.ToLongTimeString();
                lock (log)
                    log.Add("[" +time + "]: " + (args as ONL.Event.LogEventArgs).message);
            };
            ONL.Event.ConnectionFailed += (obj, args) =>
            {
                Console.WriteLine("ConnectionFailed!");
                string time = DateTime.Now.ToLongTimeString();
                lock (log)
                    log.Add("[" + time + "]: " + "ConnectionFailed!");
            };
            ONL.Event.Disconnection += (obj, args) =>
            {
                Console.WriteLine((args as ONL.Event.DisconnectEventArgs).endpoint.IpAddress.ToString() + ":" + (args as ONL.Event.DisconnectEventArgs).endpoint.Port + " disconnected: " + (args as ONL.Event.DisconnectEventArgs).reason);
                string time = DateTime.Now.ToLongTimeString();
                lock (log)
                    log.Add("[" + time + "]: " + (args as ONL.Event.DisconnectEventArgs).endpoint.IpAddress.ToString() + ":" + (args as ONL.Event.DisconnectEventArgs).endpoint.Port + " disconnected: " + (args as ONL.Event.DisconnectEventArgs).reason);

            };
            ONL.Event.ConnectionRequest += (obj, args) =>
            {
                var newArgs = args as ONL.Event.ConnectionRequestEventArgs;
                newArgs.accepted = true;
            };
            ONL.Event.ConnectionEstablished += (obj, args) =>
            {
                Console.WriteLine((obj as OakNetEndPoint).IpAddress + ":" + (obj as OakNetEndPoint).Port + " Connected!");
                string time = DateTime.Now.ToLongTimeString();
                lock (log)
                    log.Add("[" + time + "]: " + (obj as OakNetEndPoint).IpAddress + ":" + (obj as OakNetEndPoint).Port + " Connected!");

            };
            ONL.Event.ConnectionLost += (obj, args) =>
            {
                Console.WriteLine("LostConnection!");
                string time = DateTime.Now.ToLongTimeString();
                lock (log)
                    log.Add("[" + time + "]: " + "LostConnection!");
            };

            ONL.setOwnGuid(Guid.NewGuid());

            // register the plugin
            ONL.registerPlugin(new TunnelPlugin());

            var ipaddress = "127.0.0.1";
            ONL.MasterServer.Connect(ipaddress, 6868);

            
        }

        public static void requestIp(string canonicalName)
        {
            Logger.log("Requesting IP for " + canonicalName);
            //TODO validate name
            TunnelPlugin.requestIpAddress(canonicalName);
        }

    }
    /*internal enum ConnectionState
    {
        Connected,
        Connecting,
        Disconnected,
    }*/
}
