using OakNetLink.Api.Communication;
using System;
using System.Linq;
using System.Collections.Generic;
using System.Text;
using OakNetLink.Api.Packets;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionFetchListPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            if (SessionManager.AvailableSessions == null || SessionManager.AvailableSessions.Count == 0)
                return new SessionListResponsePacket() { Sessions = "" };

            string sessions = SessionManager.AvailableSessions.FirstOrDefault().Name;
            foreach(var session in SessionManager.AvailableSessions.Skip(1))
            {
                sessions += ";" + session.Name;
            }
            return new SessionListResponsePacket() { Sessions = sessions };
        }
    }
}
