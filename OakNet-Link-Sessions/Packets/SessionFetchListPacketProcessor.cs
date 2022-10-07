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
        public override PacketBase ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            if (SessionManager.AvailableSessions == null || SessionManager.AvailableSessions.Count == 0)
                return new SessionListResponsePacket() { SessionData = new byte[] { 0, 0, 0, 0 } };

            var writer = new BinaryWriter(new MemoryStream());
            writer.Write(SessionManager.AvailableSessions.Count);
            foreach(var session in SessionManager.AvailableSessions)
            {
                writer.Write(session.Name != null ? session.Name : "");         // Session Name
                writer.Write(session.HasPassword ? (byte)1 : (byte)0);          // Has Password
                writer.Write(session.Payload!=null? session.Payload.Length : 0);// Payload Length
                if (session.Payload != null)
                    writer.Write(session.Payload);                              // Payload
            }
            return new SessionListResponsePacket() { SessionData = ((MemoryStream) writer.BaseStream).ToArray() };
        }
    }
}
