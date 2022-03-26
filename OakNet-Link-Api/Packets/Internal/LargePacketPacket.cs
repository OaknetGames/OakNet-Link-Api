using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets.Internal
{
    internal class LargePacketPacket : Packet
    {
        public byte[] data;
        public short lastPacket;
    }
}
