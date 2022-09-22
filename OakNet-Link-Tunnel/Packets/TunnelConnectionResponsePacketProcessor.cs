using OakNetLink.Api;
using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class TunnelConnectionResponsePacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var responsePacket = packet as TunnelConnectionResponsePacket;
            if (responsePacket == null) return null;

            if(responsePacket.Message == "accepted")
            {
                Logger.log("other Endpoint accepted the connection!");
                var newEndpoint = ONL.Endpoint.ConnectToEndpoint(responsePacket.Address, responsePacket.Port, new Guid(responsePacket.PeerID));
                TunnelConnectionHelper.addEndpoint(responsePacket.IP, newEndpoint);
            }
            else
            {
                Logger.log("other Endpoint rejected the connection: " + responsePacket.Message);
                TunnelConnectionHelper.removeAddress(responsePacket.IP);
            }

            return null;
        }
    }
}
