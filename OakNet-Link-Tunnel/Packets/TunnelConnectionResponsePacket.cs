using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class TunnelConnectionResponsePacket : Packet
    {
        public string Address { get; set; }
        public int IP { get; set; }
        public int Port { get; set; }
        public string Message { get; set; }
    }
}
