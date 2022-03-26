using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class IPPacketDataPacket : Packet
    {
        public byte[] Data { get; set; }
    }
}
