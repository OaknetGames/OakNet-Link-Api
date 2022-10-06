using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets
{
    public class PongPacketProcessor : PacketProcessor
    {
        public override PacketBase ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            var pongPacket = (PongPacket) packet;
            endpoint.Ping = Environment.TickCount - pongPacket.timestamp;
            return null;
        }
    }
}
