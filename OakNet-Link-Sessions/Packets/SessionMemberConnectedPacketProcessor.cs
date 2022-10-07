using OakNetLink.Api.Communication;
using System;
using System.Linq;
using System.Collections.Generic;
using System.Net;
using System.Text;
using OakNetLink.Api.Packets;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionMemberConnectedPacketProcessor : PacketProcessor
    {
        public override PacketBase? ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            var sessionMemberConnectedPacket = packet as SessionMemberConnectedPacket;
            if (sessionMemberConnectedPacket == null)
                return null;
            var meberData = sessionMemberConnectedPacket!.memberData!;
            var reader = new BinaryReader(new MemoryStream(meberData));

            var address = new IPAddress(reader.ReadBytes(4));
            var port = reader.ReadInt32();
            var guid = new Guid(reader.ReadBytes(16));
            var newEndpoint = OakNetEndPointManager.Notify(address, port, guid);
            newEndpoint.ConnectionState = ConnectionState.Connecting;
            newEndpoint.tick();
            SessionManager.ActiveSession?.oakNetEndPoints.Add(newEndpoint);
            return null;
        }
    }
}
