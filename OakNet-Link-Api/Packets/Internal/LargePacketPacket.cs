using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets.Internal
{
    internal class LargePacketPacket : PacketBase
    {
        public byte[] data;
        public short lastPacket;
    }
}
