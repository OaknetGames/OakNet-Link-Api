using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionListResponsePacket : PacketBase
    {

        public byte[]? SessionData { get; set; }

        public override PacketBase? ProcessPacket(OakNetEndPoint endpoint)
        {
            if(SessionData == null)
                return null;

            var reader = new BinaryReader(new MemoryStream(SessionData));
            SessionManager.AvailableSessions.Clear();
            var sessionCount = reader.ReadInt32();
            for (var i = 0; i < sessionCount; i++)
            {
                var session = new Session();
                session.Name = reader.ReadString();
                session.HasPassword = reader.ReadByte() == 1;
                session.CurrentPlayerCount = reader.ReadInt32();
                session.MaxPlayers = reader.ReadInt32();
                session.Payload = reader.ReadBytes(reader.ReadInt32());
                SessionManager.AvailableSessions.Add(session);
            }
            SessionsPlugin.Event.SessionListUpdated?.Invoke(null, EventArgs.Empty);
            return null;
        }
    }
}
