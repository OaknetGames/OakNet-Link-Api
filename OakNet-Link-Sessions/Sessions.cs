using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using OakNetLink.Sessions.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OakNetLink.Api;
using OakNetLink.Sessions.Packets;

namespace OakNetLink.Sessions
{
    public class SessionsPlugin : ONLPlugin
    {
        public SessionsPlugin() : base(1)
        {
            ONL.Event.Disconnection += (sender, args) => {
                if (args is ONL.Event.DisconnectEventArgs disargs)
                    SessionManager.EndPointLeft(disargs.endpoint);
            };
        }


        public override Dictionary<Type, Type> registerPackets()
        {
            var result = new Dictionary<Type, Type>();
            result.Add(typeof(SessionCreatePacket), typeof(SessionCreatePacketProcessor));
            result.Add(typeof(SessionCreateResponsePacket), typeof(SessionCreateResponsePacketProcessor));
            result.Add(typeof(SessionFetchListPacket), typeof(SessionFetchListPacketProcessor));
            result.Add(typeof(SessionListResponsePacket), typeof(SessionListResponsePacketProcessor));
            result.Add(typeof(SessionJoinRequestPacket), typeof(SessionJoinRequestPacketProcessor));
            result.Add(typeof(SessionJoinRequestResponsePacket), typeof(SessionJoinRequestResponsePacketProcessor));
            result.Add(typeof(SessionMemberConnectedPacket), typeof(SessionMemberConnectedPacketProcessor));
            result.Add(typeof(SessionLeftPacket), typeof(SessionLeftPacketProcessor));
            return result;
        }


        public static class Event
        {
            /// <summary>
            /// This EventHandler will be called when the requested session creation process was successfull
            /// </summary>
            public static EventHandler? SessionCreationSuccess { get; set; }

            /// <summary>
            /// This EventHandler will be called when the requested session creation failed
            /// The passed object is the the errorMessage
            /// </summary>
            public static EventHandler? SessionCreationFailed { get; set; }

            /// <summary>
            /// This EventHandler will be called when the session list has been updated
            /// </summary>
            public static EventHandler? SessionListUpdated { get; set; }

            /// <summary>
            /// This EventHandler will be called when the session join request has been denied
            /// The passed object is the message as string
            /// </summary>
            public static EventHandler? SessionJoinDenied { get; set; }

            /// <summary>
            /// This EventHandler will be called when the session join was successfull
            /// </summary>
            public static EventHandler? SessionJoinSuccess { get; set; }
        }

        public static List<Session> AvailableSessions()
        {
            return SessionManager.AvailableSessions;
        }

        public static Session? ActiveSession()
        {
            return SessionManager.ActiveSession;
        }

        public static void FetchSessions()
        {
            SessionManager.FetchSessions();
        }

        public static void CreateNewSession(string name, string password, int maxPlayers, byte[] payload)
        {
            var createSessionPacket = new SessionCreatePacket();
            createSessionPacket.SessionName = name.Replace(";", "_");
            createSessionPacket.SessionPassword = password;
            createSessionPacket.MaxPlayers = maxPlayers;
            createSessionPacket.Payload = payload;
            var session = new Session();
            session.Name = name;
            session.Password = password;
            session.MaxPlayers = maxPlayers;
            session.Payload = payload;
            SessionManager.TrialSession  = session;
            Communicator.instance.sendPacket(PacketProcessor.EncodePacket(createSessionPacket), ONL.MasterServer.EndPoint, false, true, false);
        }

        public static void JoinSession(Session session, string password)
        {
            SessionManager.TrialSession = session;
            var sessionJoinRequestPacket = new SessionJoinRequestPacket();
            sessionJoinRequestPacket.SessionName = session.Name.Replace(";", "_");
            sessionJoinRequestPacket.SessionPassword = password;
            Communicator.instance.sendPacket(PacketProcessor.EncodePacket(sessionJoinRequestPacket), ONL.MasterServer.EndPoint, false, true, false);
        }

        public static void LeaveActiveSession(string msg = "Left Session")
        {
            ActiveSession()?.oakNetEndPoints.ToList().ForEach(p => { p.Disconnect(msg); });
            var sessionLeftPacket = new SessionLeftPacket();
            ONL.MasterServer.SendPacket(sessionLeftPacket, true);
            SessionManager.ActiveSession = null;
        }

        /// <summary>
        /// Use this function to send a broadcast to all endpoints in the session, except yourself
        /// </summary>
        public static void SendBroadcast(OakNetLink.Api.Packets.PacketBase packet, bool reliable)
        {
            foreach (var endpoint in OakNetEndPointManager.ConnectedEndpoints())
            {
                Communicator.instance.sendPacket(PacketProcessor.EncodePacket(packet), endpoint, true, reliable, false);
            }
        }

        /// <summary>
        /// Use this function to send a packet to a specific endpoint in the session
        /// </summary>
        public static void SendPacket(OakNetLink.Api.Packets.PacketBase packet, bool reliable, OakNetEndPoint receiver)
        {
            if (!OakNetEndPointManager.ConnectedEndpoints().Contains(receiver))
                return;
            Communicator.instance.sendPacket(PacketProcessor.EncodePacket(packet), receiver, false, reliable, false);
        }

    }
}
