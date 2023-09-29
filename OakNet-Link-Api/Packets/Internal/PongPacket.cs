using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets
{
    internal class PongPacket : PacketBase
    {
        public int Timestamp { get; set; }

        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            endpoint.Ping = Environment.TickCount - Timestamp;
            return null;
        }
    }
}
