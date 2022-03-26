using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class IPPacketDataPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var IPPacket = packet as IPPacketDataPacket;
            if (IPPacket == null) return null;
            TunDriverWrapper.sendPacketData(IPPacket.Data);
            return null;
        }
    }
}
