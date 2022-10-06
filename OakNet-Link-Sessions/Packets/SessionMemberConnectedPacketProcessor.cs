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
            var address = IPAddress.Parse(sessionMemberConnectedPacket.ConnectedMember.Substring(0, sessionMemberConnectedPacket.ConnectedMember.LastIndexOf(":")));
            var port = Convert.ToInt32(sessionMemberConnectedPacket.ConnectedMember.Split(':').Last());
            //var newEndpoint = OakNetEndPointManager.Notify(address, port);
            //newEndpoint.ConnectionState = ConnectionState.Connecting;
            //newEndpoint.tick();
            return null;
        }
    }
}
