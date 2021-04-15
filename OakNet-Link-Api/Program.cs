using OakNet_Link_Api.Packets;
using OakNetLink;
using OakNetLink.Communication;
using System;
using System.Linq;
using System.Threading.Tasks;
using TestClient;
using TestClient.Packets;

namespace OakNet_Link_Api
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                // Init Api
                ONL.Init();

                // Register all the EventHandler
                ONL.Event.Log += (obj, args) => Console.WriteLine((args as ONL.Event.LogEventArgs).message);
                ONL.Event.ConnectionFailed += (obj, args) => Console.WriteLine("ConnectionFailed!");
                ONL.Event.Disconnection += (obj, args) => Console.WriteLine((args as ONL.Event.DisconnectEventArgs).endpoint.IpAddress.ToString() + ":" + (args as ONL.Event.DisconnectEventArgs).endpoint.Port + " disconnected: " + (args as ONL.Event.DisconnectEventArgs).reason);
                ONL.Event.ConnectionEstablished += (obj, args) => Console.WriteLine((obj as OakNetEndPoint).IpAddress + ":" + (obj as OakNetEndPoint).Port + " Connected!");
                ONL.Event.ConnectionLost += (obj, args) => Console.WriteLine("LostConnection!");
                ONL.Event.SessionCreationFailed += (obj, args) => Console.WriteLine("Failed Creating Session!: " + obj as string);
                ONL.Event.SessionCreationSuccess += (obj, args) => Console.WriteLine("Session Creation Sucessfully!");
                ONL.Event.SessionJoinDenied += (obj, args) => Console.WriteLine("Failed Joining Session!: " + obj as string);
                ONL.Event.SessionJoinSuccess += (obj, args) =>
                {
                    Console.WriteLine("Session Joined Sucessfully!");
                    Task.Factory.StartNew(() => DummyServer.startDummyServer());
                };
                ONL.Event.SessionListUpdated += (obj, args) =>
                {
                    Console.WriteLine("Active Sessions:");
                    foreach (var sessionName in ONL.Sessions.AvailableSessions().Select((session) => session.Name))
                    {
                        Console.WriteLine(sessionName);
                    }
                };

                // Register all Packets
                ONL.Packet.RegisterPacket(typeof(MessagePacket), typeof(MessagePacketProcessor));
                ONL.Packet.RegisterPacket(typeof(McDataClientServerPacket), typeof(McDataClientServerPacketProcessor));
                ONL.Packet.RegisterPacket(typeof(McDataServerClientPacket), typeof(McDataServerClientPacketProcessor));
                ONL.Packet.RegisterPacket(typeof(McClientDisconnectionPacket), typeof(McClientDisconnectionPacketProcessor));

                var ipaddress = "195.201.156.41";
                if(args.Length != 0)
                    ipaddress = args[0];
                ONL.MasterServer.Connect(ipaddress, 6868);
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
                            ONL.Sessions.CreateNewSession(cmdArgs[1], "");
                            DummyClient.isHost = true;
                            break;
                        case "list":
                            ONL.Sessions.FetchSessions();
                            break;
                        case "join":
                            if (cmdArgs.Length == 1)
                            {
                                Console.WriteLine($"You have to specify a name: join <sessionname>");
                                break;
                            }
                            ONL.Sessions.JoinSession(cmdArgs[1], "");
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
