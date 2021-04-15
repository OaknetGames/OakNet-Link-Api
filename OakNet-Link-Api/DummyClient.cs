using OakNet_Link_Api.Packets;
using OakNetLink;
using OakNetLink.Communication;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using TestClient.Packets;

// The SocketHandling in this class isn't done great, should be reworked
namespace TestClient
{
    public class DummyClient
    {
        public static bool isHost = false;
        public static Dictionary<OakNetEndPoint, DummyClient> dict = new Dictionary<OakNetEndPoint, DummyClient>();

        public OakNetEndPoint receiver;
        public Socket socket;

        public static DummyClient getDummyClient(OakNetEndPoint ep)
        {
            lock (dict)
            {
                if (!dict.ContainsKey(ep))
                {
                    dict.Add(ep, new DummyClient(ep));
                }
            }

            return dict[ep];

        }

        public DummyClient(OakNetEndPoint receiver) 
        {
            this.receiver = receiver;

            // Establish the remote endpoint for the socket.  
            IPAddress ipAddress = IPAddress.Loopback;
            IPEndPoint remoteEP = new IPEndPoint(ipAddress, 25565);

            // Create a TCP/IP  socket.  
            Socket sender = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            // Connect the socket to the remote endpoint. Catch any errors.  
            try
            {
                sender.Connect(remoteEP);

                socket = sender;
                var buffer = new byte[500];
                Task.Factory.StartNew(() =>
                {
                    bool connected = true;
                    while (connected)
                    {
                        while (connected && socket != null)
                        {
                            try
                            {
                                if (sender.Poll(1000, SelectMode.SelectRead))
                                {
                                    if (sender.Available == 0)
                                        connected = false;
                                    break;
                                }
                                else
                                    Thread.Sleep(1);
                            }
                            catch (Exception)
                            {
                                connected = false;
                                break;
                            }
                        }

                        if (!connected)
                            break;

                        try
                        {
                            while (sender.Available > 0)
                            {
                                if (sender.Available > 500)
                                    buffer = new byte[500];
                                else
                                    buffer = new byte[sender.Available];
                                var bytesRead = sender.Receive(buffer);
                                ONL.Sessions.SendBroadcast(new McDataServerClientPacket() { data = buffer }, true);
                            }
                        }
                        catch (ObjectDisposedException)
                        {
                            connected = false;
                            break;
                        }
                        
                    }
                    socket = null;
                    // Release the socket.  
                    try
                    {
                        sender.Shutdown(SocketShutdown.Both);
                        sender.Close();
                    }
                    catch (Exception) { }
                    lock (dict)
                    {
                        dict.Remove(receiver);
                    }
                });
            }
            catch (ArgumentNullException ane)
            {
                Console.WriteLine("ArgumentNullException : {0}", ane.ToString());
            }
            catch (SocketException se)
            {
                Console.WriteLine("SocketException : {0}", se.ToString());
            }
            catch (Exception e)
            {
                Console.WriteLine("Unexpected exception : {0}", e.ToString());
            }
        }

        public void sendData(byte[] data)
        {
            try
            {   if (socket != null)
                    socket.Send(data);
            }
            catch (SocketException e) { }
        }
    }
}
