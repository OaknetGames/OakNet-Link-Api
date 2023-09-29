using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets
{
    internal class PingPacket : PacketBase
    {
        public int Timestamp { get; set; }

        public override PacketBase ProcessPacket(OakNetEndPoint endPoint)
        {
            var pongPacket = new PongPacket();
            pongPacket.Timestamp = Timestamp;
            return pongPacket;
        }
    }
}
