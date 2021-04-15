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
    public class DummyServer
    {
        public static Socket currentOutgoingSocket;

        public static void startDummyServer()
        {
            Console.WriteLine("Starting Dummy Server on Port 25566");
            // Data buffer for incoming data.  
            byte[] bytes = new Byte[1024];

            // Establish the local endpoint for the socket.  
            // Dns.GetHostName returns the name of the
            // host running the application.  
            IPAddress ipAddress = IPAddress.Loopback;
            IPEndPoint localEndPoint = new IPEndPoint(ipAddress, 25566);

            // Create a TCP/IP socket.  
            Socket listener = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            // Bind the socket to the local endpoint and
            // listen for incoming connections.  
            try
            {
                listener.Bind(localEndPoint);
                listener.Listen(1);
                var buffer = new byte[500];
                // Start listening for connections.  
                while (true)
                { 
                    Socket handler = listener.Accept();
                    currentOutgoingSocket = handler;
                    // An incoming connection needs to be processed.  
                    bool connected = true;
                    while (connected)
                    {
                        while (connected)
                        {
                            try
                            {
                                if (handler.Poll(1000, SelectMode.SelectRead))
                                {
                                    if (handler.Available == 0)
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
                            while (handler.Available > 0)
                            {
                                if (handler.Available > 500)
                                    buffer = new byte[500];
                                else
                                    buffer = new byte[handler.Available];
                                var bytesRead = handler.Receive(buffer);
                                ONL.Sessions.SendBroadcast(new McDataClientServerPacket() { data = buffer }, true);
                            }
                        } 
                        catch (ObjectDisposedException)
                        {
                            connected = false;
                            break;
                        }
                        
                    }
                    currentOutgoingSocket = null;
                    try
                    {
                        handler.Shutdown(SocketShutdown.Both);
                        handler.Close();
                    }
                    catch (Exception) { }

                    ONL.Sessions.SendBroadcast(new McClientDisconnectionPacket(), true);
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        public static void sendData(byte[] data)
        {
            try
            {
                if(currentOutgoingSocket != null)
                    currentOutgoingSocket.Send(data);
            }
            catch (SocketException e) {
                currentOutgoingSocket = null;
            }
        }

    }
}
