using OakNetLink;
using OakNetLink.Api;
using OakNetLink.Api.Communication;
using OakNetLink.Tunnel;
using OakNetLink.Tunnel.Packets;
using System;
using System.Linq;
using System.Runtime.InteropServices;
using System.Threading;
using System.Threading.Tasks;
/*
byte[] packet_bytes = new byte[]{
  0x45, 0x00, 0x00, 0x3c, 0x94, 0xca, 0x00, 0x00,
  0x80, 0x01, 0x91, 0xe4, 0x0a, 0x06, 0x00, 0x03,
  0x0a, 0x06, 0x00, 0x04, 0x08, 0x00, 0x49, 0xcd,
  0x00, 0x01, 0x03, 0x8e, 0x61, 0x62, 0x63, 0x64,
  0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c,
  0x6d, 0x6e, 0x6f, 0x70, 0x71, 0x72, 0x73, 0x74,
  0x75, 0x76, 0x77, 0x61, 0x62, 0x63, 0x64, 0x65,
  0x66, 0x67, 0x68, 0x69
};
*/

namespace OakNetLink.Tunnel
{
    public class Tunnel
    {
        [STAThread]
        static void Main(string[] args)
        {
            try
            {
                //TunDriverWrapper.createAdapter((10 << 24) | (6 << 16) | (0 << 8) | 4);
                // Register all the EventHandler
                ONL.Event.Log += (obj, args) => Console.WriteLine((args as ONL.Event.LogEventArgs).message);
                ONL.Event.ConnectionFailed += (obj, args) => Console.WriteLine("ConnectionFailed!");
                ONL.Event.Disconnection += (obj, args) => Console.WriteLine((args as ONL.Event.DisconnectEventArgs).endpoint.IpAddress.ToString() + ":" + (args as ONL.Event.DisconnectEventArgs).endpoint.Port + " disconnected: " + (args as ONL.Event.DisconnectEventArgs).reason);
                ONL.Event.ConnectionEstablished += (obj, args) => Console.WriteLine((obj as OakNetEndPoint).IpAddress + ":" + (obj as OakNetEndPoint).Port + " Connected!");
                ONL.Event.ConnectionLost += (obj, args) => Console.WriteLine("LostConnection!");

                // register our plugin
                ONL.registerPlugin(new TunnelPlugin());

                var ipaddress = "192.168.2.42";
                if(args.Length != 0)
                    ipaddress = args[0];
                ONL.MasterServer.Connect(ipaddress, 6868);

                TunnelPlugin.requestIpAddress(Console.ReadLine());

                while (true)
                {
                    var cmdArgs = Console.ReadLine().Split(' ');
                    switch (cmdArgs[0])
                    {
                        case "help":
                            Console.WriteLine("Available Commands: ");
                            Console.WriteLine("help: Display this help screen");
                            Console.WriteLine("ping: Show the ping to the MasterServer");
                            Console.WriteLine("create <sessionname>: Creates a new session");
                            Console.WriteLine("list: List all available sessions");
                            Console.WriteLine("join <sessionname>: joins the specified session");
                            Console.WriteLine("exit: Closes the program");
                            break;
                        case "ping":
                            Console.WriteLine($"The current Ping is {ONL.MasterServer.EndPoint.Ping}");
                            break;
                        case "create":
                            if (cmdArgs.Length == 1)
                            {
                                Console.WriteLine($"You have to specify a name: create <sessionname>");
                                break;
                            }
                            break;
                        case "list":
                            break;
                        case "join":
                            if (cmdArgs.Length == 1)
                            {
                                Console.WriteLine($"You have to specify a name: join <sessionname>");
                                break;
                            }
                            break;
                        case "exit":
                            Environment.Exit(0);
                            break;
                        default:
                            Console.WriteLine("Unknown command use \"help\" for help.");
                            break;
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                Console.ReadLine();
            }
        }
    }
}
