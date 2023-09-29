using System;
using System.Net.Sockets;
using System.Net;
using System.Threading;
using System.Threading.Tasks;
using System.Runtime.InteropServices;
using OakNetLink.Api.Packets.Internal;
using OakNetLink.Api.Packets;
using System.IO;

namespace OakNetLink.Api.Communication
{
    public class Communicator
    {

        UdpClient udpClient;

        internal bool isServer;
        internal bool allowBroadcasts;
        internal Guid ownID;

        public static Communicator instance;


        public Communicator(Guid ownID, int port = 0, bool server = false, bool allowBroadcasts = true){
            //Logger.log("Creating new Communicator");
            instance = this;
            this.ownID = ownID;
            isServer = server;
            this.allowBroadcasts = allowBroadcasts;
            udpClient = new UdpClient(AddressFamily.InterNetwork);
            var localEndPoint = new IPEndPoint(IPAddress.Any, port);
            udpClient.Client.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.ReuseAddress, true);
            //FIX FOR MICROSOFT STUPIDNESS
            if (RuntimeInformation.IsOSPlatform(OSPlatform.Windows))
            {
                var sioUdpConnectionReset = -1744830452;
                var inValue = new byte[] { 0 };
                var outValue = new byte[] { 0 };
                udpClient.Client.IOControl(sioUdpConnectionReset, inValue, outValue);
                udpClient.AllowNatTraversal(true);
            }
            udpClient.Client.Bind(localEndPoint);

            var networkThread = new Thread(() =>
            {
                while (true)
                {
                    //IPEndPoint object will allow us to read datagrams sent from any source.
                    var remoteIpEndPoint = new IPEndPoint(IPAddress.Any, 0);

                    byte[] buffer = new byte[0];
                    try
                    {
                        // Blocks until a message returns on this socket from a remote host.
                        buffer = udpClient.Receive(ref remoteIpEndPoint);
                    } 
                    catch (SocketException e)
                    {
                        Logger.log("Exception in the udpSocket: " + e.Message);
                        continue;
                    }

                    var reader = new BinaryReader(new MemoryStream(buffer));

                    // decode bytes to short
                    var packetLength = reader.ReadUInt16();
                    // extract the 7 status bits
                    var status = (byte)((packetLength >> 8) & 0b11111110);
                    // remove the status bits from the packet length 
                    packetLength = (ushort)(packetLength & 0b00000001_11111111);
                    // extract the individual status flags
                    var broadcast = ((byte)((status & 0b10000000) >> 7)) == 1;
                    var reliable = ((byte)((status & 0b01000000) >> 6)) == 1;
                    var ack = ((byte)((status & 0b00100000) >> 5)) == 1;

                    // read sender GUID
                    var guidBytes = reader.ReadBytes(16);
                    var peerID = new Guid(guidBytes);
                    var endPoint = OakNetEndPointManager.Notify(remoteIpEndPoint.Address, remoteIpEndPoint.Port, peerID);
                    endPoint.lastReceived = DateTime.Now;
                    
                    // Handle special packets
                    if (reliable)
                    {
                        var reliableID = reader.ReadInt32();
                        // send ack
                        ackReliable(reliableID, endPoint);
                        endPoint.addReliableIn(reliableID, reader.ReadBytes((int)(reader.BaseStream.Length - reader.BaseStream.Position)), broadcast);
                        continue;
                    }

                    if (ack)
                    {
                        var reliableID = reader.ReadInt32();
                        endPoint.ackReliable(reliableID);
                        continue;
                    }

                    // Verify that exactly packetLength bytes were read
                    //if (buffer.Length != packetLength + 2 )
                    //disconnect("Recieved wrong number of bytes");

                    //Handle Broadcasts as Server
                    byte[] copy = (byte[])buffer.Clone();
                    if (Communicator.instance.isServer && broadcast)
                    {
                        if (!allowBroadcasts)
                            continue;
                        foreach (var endpoint in OakNetEndPointManager.ConnectedEndpoints())
                        {
                            sendPacket(new BinaryWriter(new MemoryStream((byte[]) buffer.Clone())), endpoint, false, reliable, false);
                        }
                        continue;
                    }

                    var packetData = reader.ReadBytes((int) (reader.BaseStream.Length - reader.BaseStream.Position));
                    //Handle Packets
                    endPoint.handlePacket(packetData, broadcast, false);
                }
            });
            networkThread.IsBackground = true;
            networkThread.Start();

            var pingThread = new Thread(() =>
            {
                while (true)
                {
                    if (OakNetEndPointManager.MasterServerEndpoint != null)
                    {
                        sendPing(OakNetEndPointManager.MasterServerEndpoint);
                        checkTimeout(OakNetEndPointManager.MasterServerEndpoint);
                    }

                    if (OakNetEndPointManager.ManagerServerEndpoint != null) { 
                        sendPing(OakNetEndPointManager.ManagerServerEndpoint);
                        checkTimeout(OakNetEndPointManager.ManagerServerEndpoint);
                    }

                    foreach (var ep in OakNetEndPointManager.ConnectedEndpoints())
                    {
                        sendPing(ep);
                        checkTimeout(ep);
                    }
                    Thread.Sleep(1000);

                    void sendPing(OakNetEndPoint endPoint)
                    {
                        var pingPacket = new PingPacket();
                        pingPacket.Timestamp = Environment.TickCount;
                        sendPacket(PacketProcessor.EncodePacket(pingPacket), endPoint, false, false, false);
                    }

                    void checkTimeout(OakNetEndPoint endPoint)
                    {
                        if (endPoint.ConnectionState != ConnectionState.Connected)
                            return;
                        if (DateTime.Now - endPoint.lastReceived > TimeSpan.FromSeconds(3))
                        {
                            endPoint.Disconnect("Timed out!");
                        }
                    }
                }
            });
            pingThread.IsBackground = true;
            pingThread.Start();
        }

        public void sendPacket(BinaryWriter packet, OakNetEndPoint receiver, bool broadcast, bool reliable, bool ack)
        {
            var metaWriter = new BinaryWriter(new MemoryStream());
            if (receiver.ConnectionState == ConnectionState.Disconnected)
                return;
            // add ownID
            metaWriter.Write(OakNetEndPointManager.ownID.ToByteArray());
            // add reliable stuff
            var reliableId = 0;
            if (reliable)
            {
                reliableId = receiver.nextReliableOutId();
                metaWriter.Write(reliableId);
            }

            // Build the status
            var status = 0b00000000;
            if (broadcast)
                status |= 0b10000000;
            if (reliable)
                status |= 0b01000000;
            if (ack)
                status |= 0b00100000;
            // we send little endian so the least significant bit is in the first byte
            packet.Flush();

            // build Packet Size + Meta + PacketData
            var data = new byte[2 + metaWriter.BaseStream.Length + packet.BaseStream.Length];
            var size = (ushort) (metaWriter.BaseStream.Length + packet.BaseStream.Length);
            data[0] = (byte) (size % 0x100);
            data[1] = (byte)(((size / 0x100) & 0b00000001) | status);
            metaWriter.BaseStream.Seek(0, SeekOrigin.Begin);
            metaWriter.BaseStream.Read(data, 2 , (int) metaWriter.BaseStream.Length);
            packet.BaseStream.Seek(0, SeekOrigin.Begin);
            packet.BaseStream.Read(data, 2 + (int) metaWriter.BaseStream.Length, (int) packet.BaseStream.Length);

            if (data.Length > 490)
            {
                LargePacketPacket.MakeLargePacket(new BinaryReader(new MemoryStream(data)), receiver, broadcast);
                return;
            }

            sendData(data, receiver);

            if (reliable)
            {
                receiver.addReliableOut(reliableId, data);
                checkAsync();
            }
            
            async void checkAsync()
            {
                await Task.Run(() =>
                {
                    Thread.Sleep((int) (receiver.Ping*1.2));
                    receiver.checkReliableOut();
                });
            }
        }

        internal Guid getGuid() => ownID;

        internal void ackReliable(int id, OakNetEndPoint endPoint)
        {
            var writer = new BinaryWriter(new MemoryStream());
            writer.Write(id);
            sendPacket(writer, endPoint, false, false, true);
        }

        internal void sendData(byte[] data, OakNetEndPoint endPoint)
        {
            // check if the socket is open
            if (udpClient == null)
            {
                //connected = false;
                return;
            }
            //else if (!socket.Connected)
            //{
            // connected = false;
            // return;
            // }
            // Send bytes
            if (endPoint.ConnectionState == ConnectionState.Disconnected)
                return;
            var receiver = new IPEndPoint(endPoint.IpAddress, endPoint.Port);
            int bytesSend = udpClient.Send(data, data.Length, receiver);

            // Verify sent Bytes
            if (bytesSend != data.Length)
                endPoint.Disconnect("Send wrong number of bytes");
        }
    }
}
