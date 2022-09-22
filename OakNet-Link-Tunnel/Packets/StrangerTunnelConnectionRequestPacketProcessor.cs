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
    internal class StrangerTunnelConnectionRequestPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var requestPacket = packet as StrangerTunnelConnectionRequestPacket;
            if (requestPacket == null) return null;
            var response = new StrangerTunnelConnectionResponsePacket();
            response.Address = requestPacket.Address;
            response.Port = requestPacket.Port;
            response.PeerID = requestPacket.PeerID;
            Logger.log($"Endpoint {requestPacket.Address}:{requestPacket.Port} requested connection");
            // TODO ask for permission, Note: 60 second time 
            if (true)
            {
                response.Response = "accepted";
                Task.Factory.StartNew(() =>
                {
                    var newEndpoint = ONL.Endpoint.ConnectToEndpoint(requestPacket.Address, requestPacket.Port, new Guid(requestPacket.PeerID));
                    TunnelConnectionHelper.addEndpoint(requestPacket.IP, newEndpoint);
                });
            }
            else
            {
                response.Response = "rejected";
            }
            return response;
        }
    }
}
