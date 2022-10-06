using System;
using System.Collections.Generic;
using System.Net;
using System.Linq;
using System.Threading.Tasks;
using System.Threading;
using OakNetLink.Api.Packets.Internal;
using OakNetLink.Api.Packets;
using OakNetLink.Api;

namespace OakNetLink.Api.Communication
{
    public class OakNetEndPoint
    {
        internal Dictionary<int, ReliablePacket> reliableOutQueue = new Dictionary<int, ReliablePacket>();
        internal Dictionary<int, ReliablePacket> reliableInQueue = new Dictionary<int, ReliablePacket>();
        internal DateTime lastReceived;
        internal List<ONLPlugin> semaphore;

        int reliableInId = 0;
        int reliableOutId = 0;

        public int Ping { get; internal set; }

        public IPAddress IpAddress { get; internal set; }

        public int Port { get; internal set; }

        public Guid PeerID { get; internal set; }

        public ConnectionState ConnectionState { get; set; }

        internal OakNetEndPoint(IPAddress iPAddress, int port, Guid peerID) {
            this.IpAddress = iPAddress;
            this.Port = port;
            this.PeerID = peerID;
            Ping = 999;
            ConnectionState = ConnectionState.Disconnected;
        }

        public void sendPacket(PacketBase packet, bool reliable)
        {
            ONL.Endpoint.SendPacket(packet, this, reliable);
        }

        internal void addReliableIn(int reliableId, byte[] buffer, bool broadcast)
        {
            lock (reliableInQueue) 
            { 
                if(!reliableInQueue.ContainsKey(reliableId))
                    reliableInQueue.Add(reliableId, new ReliablePacket() { broadcast = broadcast, packetData = buffer }); 
            }
            handleReliablesIn();
        }

        private void handleReliablesIn()
        {
            foreach (var kvp in reliableInQueue.AsEnumerable())
            {
                if(kvp.Key == reliableInId)
                {
                    if (reliableInId == Int32.MaxValue)
                        reliableInId = -1;
                    reliableInId++;
                    var packet = kvp.Value;
                    handlePacket(packet.packetData, packet.broadcast, true);
                    lock (reliableInQueue)
                    {
                        reliableInQueue.Remove(kvp.Key);
                    }
                    handleReliablesIn();
                    break;
                }
            }
        }

        internal void handlePacket(byte[] buffer, bool broadcast, bool reliable)
        {
            var answer = PacketProcessor.DecodePacket(buffer, this);
            if (broadcast)
            {
                if (answer != null)
                    foreach (var ep in OakNetEndPointManager.ConnectedEndpoints())
                    {
                        Communicator.instance.sendPacket(PacketProcessor.EncodePacket(answer), ep, true, reliable, false);
                    }
            }
            else
                if (answer != null)
                Communicator.instance.sendPacket(PacketProcessor.EncodePacket(answer), this, false, reliable, false);
        }

        internal void checkReliableOut()
        {
            lock (reliableOutQueue)
            {
                if (reliableOutQueue.Values.Count == 0)
                    return;
                if (Environment.TickCount - reliableOutQueue.Values.First().timestamp < 100)
                    return;
                foreach (var packet in reliableOutQueue.Values)
                {
                    if (Environment.TickCount - reliableOutQueue.Values.First().timestamp < 100)
                        return;
                    Communicator.instance.sendData(packet.packetData, this);

                }
            }
            
            checkAsync();
            async void checkAsync()
            {
                await Task.Run(() =>
                {
                    Thread.Sleep(Ping*2);
                    //TODO I think this is stupid... Stack overflow possible?
                    checkReliableOut();
                });
            }
        }

        internal void ackReliable(int reliableId)
        {
            lock (reliableOutQueue)
            {
                if (reliableOutQueue.ContainsKey(reliableId))
                    reliableOutQueue.Remove(reliableId);
            }
        }

        internal int nextReliableOutId()
        {
            if (reliableOutId == Int32.MaxValue)
                reliableOutId = -1;
            return reliableOutId++;
        }

        internal void addReliableOut(int reliableID, byte[] packetData)
        {
            lock (reliableOutQueue)
            {
                reliableOutQueue.Add(reliableID, new ReliablePacket() { timestamp = Environment.TickCount, packetData = packetData });
            }
        }

        int connectionTries = 0;
        public void tick()
        {
            switch (ConnectionState)
            {
                case ConnectionState.Disconnected:
                    lock (reliableInQueue)
                    {
                        reliableInQueue.Clear();
                    }
                    lock (reliableOutQueue)
                    {
                        reliableOutQueue.Clear();
                    }
                    reliableInId = 0;
                    reliableOutId = 0;
                    connectionTries = 0;
                    break;
                case ConnectionState.Connecting:
                    var connectionRequestPacket = new ConnectionRequestPacket();
                    
                    Task.Factory.StartNew(() =>
                    {
                        while(ConnectionState != ConnectionState.Connected)
                        {
                            Communicator.instance.sendPacket(PacketProcessor.EncodePacket(connectionRequestPacket), receiver: this, broadcast: false, reliable: false, ack: false);
                            Thread.Sleep(250);
                            if (connectionTries > 20)
                            {
                                if (ConnectionState != ConnectionState.Connected)
                                {
                                    ConnectionState = ConnectionState.Disconnected;
                                    connectionTries = 0;
                                    ONL.Event.ConnectionFailed.Invoke(this, null);
                                }
                                break;
                            }
                            else
                            {
                                connectionTries++;
                            }
                        }
                    });
                    break;
                case ConnectionState.Connected:
                    connectionTries = 0;
                    break;
            }
        }

       
    }
}
