
using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Net;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    class SessionCreateResponsePacketProcessor : PacketProcessor
    {
        public override Packet? processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var sessionCreateResponsePacket = packet as SessionCreateResponsePacket;

            if(sessionCreateResponsePacket?.responseMessage != "Success")
                Sessions.Event.SessionCreationFailed?.Invoke(sessionCreateResponsePacket?.responseMessage, EventArgs.Empty);
            else
                Sessions.Event.SessionCreationSuccess?.Invoke(null, EventArgs.Empty);

            return null;
        }
    }
}
