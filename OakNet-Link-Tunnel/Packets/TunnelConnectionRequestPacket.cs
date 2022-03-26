using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class TunnelConnectionRequestPacket : Packet
    {
        public int Address { get; set; }
    }
}
