using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets
{
    public class PongPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var pongPacket = (PongPacket) packet;
            endpoint.Ping = Environment.TickCount - pongPacket.timestamp;
            return null;
        }
    }
}
