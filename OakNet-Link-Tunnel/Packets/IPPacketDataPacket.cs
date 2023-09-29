using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class IPPacketDataPacket : PacketBase
    {
        public byte[] Data { get; set; }
        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            TunDriverWrapper.sendPacketData(Data);
            return null;
        }
    }
}
