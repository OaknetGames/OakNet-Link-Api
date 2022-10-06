using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using OakNetLink.Api.Packets.Internal;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;

namespace OakNetLink.Api
{
    public static class ONL
    {

        /// <summary>
        /// This is required to add your own Packets to the System
        /// </summary>
        public static void registerPlugin(ONLPlugin plugin)
        {
            PluginManager.addPlugin(plugin);
        }

        /// <summary>
        ///  This sets the clients own PeerId
        /// </summary>
        /// <param name="ownId">The Guid to set</param>
        public static void setOwnGuid(Guid ownId)
        {
            OakNetEndPointManager.ownID = ownId;
        }

        public static partial class Event
        {
            public class LogEventArgs : EventArgs
            {
                public string message;

                public LogEventArgs(string msg)
                {
                    message = msg;
                }
            }
            public class ConnectionRequestEventArgs : EventArgs
            {
                public bool accepted=false;
                public Guid peerId;

                public ConnectionRequestEventArgs(Guid peerId)
                {
                    this.peerId = peerId;
                }
            }

            public class DisconnectEventArgs : EventArgs
            {
                public string reason;
                public OakNetEndPoint endpoint;

                public DisconnectEventArgs(OakNetEndPoint endpoint, string reason)
                {
                    this.reason = reason;
                    this.endpoint = endpoint;
                }
            }

            /// <summary>
            /// This EventHandler ist used to handle our logging its receiving LogEventArgs which contain the messages 
            /// </summary>
            public static EventHandler Log { get; set; }

            /// <summary>
            /// This EventHandler will be called when an endpoint wants to connect
            /// It receives ConnectionRequestEventArgs
            /// </summary>
            public static EventHandler ConnectionRequest { get; set; }
            /// <summary>
            /// This EventHandler will be called when an endpoint connects
            /// The passed object is the OakNetEndpoint
            /// </summary>
            public static EventHandler ConnectionEstablished { get; set; }

            /// <summary>
            /// This EventHandler will be called when an endpoint losts its connection
            /// The passed object is the OakNetEndpoint
            /// </summary>
            public static EventHandler ConnectionLost { get; set; }

            /// <summary>
            /// This EventHandler will be called when a endpoint failes connecting
            /// The passed object is the OakNetEndpoint
            /// </summary>
            public static EventHandler ConnectionFailed { get; set; }

            /// <summary>
            /// This EventHandler will be called when an endpoint disconnects
            /// It receives DisconnectEventArgs
            /// </summary>
            public static EventHandler Disconnection { get; set; }

        }


        public static class Endpoint
        {
            public static OakNetEndPoint ConnectToEndpoint(string ip, int port, Guid peerID)
            {
                if (Communicator.instance == null)
                    new Communicator(OakNetEndPointManager.ownID);
                Logger.log("Connecting to Endpoint: "+ ip + ":" + port);
                var newEndpoint = OakNetEndPointManager.Notify(IPAddress.Parse(ip), port, peerID);
                newEndpoint.ConnectionState = ConnectionState.Connecting;
                newEndpoint.tick();
                return newEndpoint;
            }

            public static void SendPacket(OakNetLink.Api.Packets.PacketBase packet, OakNetEndPoint endPoint, bool reliable)
            {
                Communicator.instance.sendPacket(PacketProcessor.EncodePacket(packet), endPoint, false, reliable, false);
            }
        }

        /// <summary>
        /// The Masterserver does all the the P2P Management stuff.
        /// You can use the hosted OakNet-Link MasterServer at oaknet.work to extend our Network
        /// or host your own to create a new OakNetLink Network.
        /// This Server needs to be rachable in the web.
        /// </summary>
        public static class MasterServer
        {
            public static OakNetEndPoint EndPoint { get { return OakNetEndPointManager.MasterServerEndpoint; } }

            /// <summary>
            /// Call this function to start a new MasterServer
            /// </summary>
            /// <param name="address"></param>
            /// <param name="port"></param>
            public static void StartServer(string address = "127.0.0.1", int port = 6868, bool allowBroadcasts = false)
            {
                new Communicator(OakNetEndPointManager.ownID, port, true, allowBroadcasts);
                Logger.log("Starting new OakNet-Link MasterServer on port " + port);
            }

            /// <summary>
            /// Use this function to connect to a MasterServer
            /// </summary>
            public static void Connect(string address = "master.oaknet.link", int port = 6868)
            {
                if (Communicator.instance == null)
                    new Communicator(OakNetEndPointManager.ownID);
                Logger.log("Connecting to OakNet-Link MasterServer");
                OakNetEndPointManager.MasterServerEndpoint = new OakNetEndPoint(Dns.GetHostAddresses(address).First(), port, Guid.Empty);
                OakNetEndPointManager.MasterServerEndpoint.ConnectionState = ConnectionState.Connecting;
                OakNetEndPointManager.MasterServerEndpoint.tick();
            }
            /// <summary>
            /// Use this function to send a broadcast to all endpoints connected to the MasterServer
            /// </summary>
            public static void SendBroadcast(OakNetLink.Api.Packets.PacketBase packet, bool reliable)
            {
                Communicator.instance.sendPacket(PacketProcessor.EncodePacket(packet), OakNetEndPointManager.MasterServerEndpoint, broadcast:true, reliable:reliable, ack:false);
            }

            public static void SendPacket(OakNetLink.Api.Packets.PacketBase packet, bool reliable)
            {
                Communicator.instance.sendPacket(PacketProcessor.EncodePacket(packet), OakNetEndPointManager.MasterServerEndpoint, broadcast:false, reliable: reliable, ack:false);
            }
        }

        /// <summary>
        /// Turn Server are used to relay traffic for peers which cannot take part in the network directly.
        /// Users which support UPnP Port Forwarding could also act as Turn Server. TODO investigate that idea further
        /// Every Turn Server will contact the MasterServer to anounce themselve in the Network
        /// </summary>
        public static class Turn
        {
            /// <summary>
            /// Call this function to start a new TurnServer
            /// </summary>
            /// <param name="address"></param>
            /// <param name="port"></param>
            public static void StartServer(string address = "127.0.0.1", int port = 6870, string masterServerAdress="master.link.oaknet.work")
            {
                new Communicator(OakNetEndPointManager.ownID, port);
                Logger.log("Starting new OakNet-Link TurnServer on port " + port);
            } 
        }
    }
}
