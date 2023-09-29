using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class TunnelConnectionRequestPacket : PacketBase
    {
        public int Address { get; set; }
        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            var receipient = TunnelPlugin.Manager.getEndPoint(Address);
            if (receipient == null)
            {
                var response = new TunnelConnectionResponsePacket();
                response.Message = "Endpoint not found!";
                response.IP = Address;
                return response;
            }
            var otherPacket = new StrangerTunnelConnectionRequestPacket();
            otherPacket.Address = endpoint.IpAddress.ToString();
            otherPacket.Port = endpoint.Port;
            otherPacket.IP = TunnelPlugin.Manager.getAddress(endpoint);
            otherPacket.PeerID = endpoint.PeerID.ToByteArray();
            receipient.sendPacket(otherPacket, true);
            return null;
        }
    }
}
