using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    public class InetAddressRequestPacket : Packet
    {
        public string CanonicalName { get; set; }
    }
}
