using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets
{
    internal class PingPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endPoint)
        {
            var pingPacket = (PingPacket)packet;
            var pongPacket = new PongPacket();
            pongPacket.timestamp = pingPacket.timestamp;
            return pongPacket;
        }
    }
}
