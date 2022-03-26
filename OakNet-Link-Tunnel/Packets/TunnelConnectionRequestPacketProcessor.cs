using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class TunnelConnectionRequestPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var requestPacket = packet as TunnelConnectionRequestPacket;
            if(requestPacket == null)
                return null;
            var receipient = TunnelPlugin.Manager.getEndPoint(requestPacket.Address);
            if (receipient == null)
            {
                var response = new TunnelConnectionResponsePacket();
                response.Message = "Endpoint not found!";
                response.IP = requestPacket.Address;
                return response;
            }
            var otherPacket = new StrangerTunnelConnectionRequestPacket();
            otherPacket.Address = endpoint.IpAddress.ToString();
            otherPacket.Port = endpoint.Port;
            otherPacket.IP = TunnelPlugin.Manager.getAddress(endpoint);
            receipient.sendPacket(otherPacket, true);
            return null;
        }
    }
}
