using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionListResponsePacketProcessor : PacketProcessor
    {
        public override Packet? processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var sessionListResponsePacket = packet as SessionListResponsePacket;
            var sessions = sessionListResponsePacket?.Sessions.Split(';');
            SessionManager.AvailableSessions.Clear();
            if(sessions != null)
                foreach (var session in sessions)
                {
                    SessionManager.AvailableSessions.Add(new Session() { Name = session });
                }
            Sessions.Event.SessionListUpdated?.Invoke(null, EventArgs.Empty);
            return null;
        }
    }
}
