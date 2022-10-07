﻿using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets
{
    internal class PingPacketProcessor : PacketProcessor
    {
        public override PacketBase ProcessPacket(PacketBase packet, OakNetEndPoint endPoint)
        {
            var pingPacket = (PingPacket)packet;
            var pongPacket = new PongPacket();
            pongPacket.Timestamp = pingPacket.Timestamp;
            return pongPacket;
        }
    }
}
