using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets
{
    internal class ReliablePacket
    {
        public int timestamp;
        public bool broadcast;
        public byte[] packetData;
    }
}
