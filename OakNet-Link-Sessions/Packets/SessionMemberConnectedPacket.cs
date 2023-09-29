using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Net;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionMemberConnectedPacket : PacketBase
    {
        public byte[]? memberData { get; set; }

        public override PacketBase? ProcessPacket(OakNetEndPoint endpoint)
        {
            if (SessionManager.ActiveSession == null)
                return new SessionLeftPacket();
            var meberData = memberData!;
            var reader = new BinaryReader(new MemoryStream(meberData));

            var address = new IPAddress(reader.ReadBytes(4));
            var port = reader.ReadInt32();
            var guid = new Guid(reader.ReadBytes(16));
            var newEndpoint = OakNetEndPointManager.Notify(address, port, guid);
            newEndpoint.ConnectionState = ConnectionState.Connecting;
            newEndpoint.tick();
            SessionManager.ActiveSession.oakNetEndPoints.Add(newEndpoint);
            SessionManager.ActiveSession.CurrentPlayerCount++;
            return null;
        }
    }
}
