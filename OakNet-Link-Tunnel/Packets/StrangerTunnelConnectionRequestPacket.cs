using OakNetLink.Api.Communication;
using OakNetLink.Api;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class StrangerTunnelConnectionRequestPacket : PacketBase
    {
        public int IP { get; set; }
        public string Address { get; set; }
        public int Port { get; set; }
        public byte[] PeerID { get; set; }

        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            var response = new StrangerTunnelConnectionResponsePacket();
            response.Address = Address;
            response.Port = Port;
            response.PeerID = PeerID;
            Logger.log($"Endpoint {Address}:{Port} requested connection");
            // TODO ask for permission, Note: 60 second time 
            if (true)
            {
                response.Response = "accepted";
                Task.Factory.StartNew(() =>
                {
                    var newEndpoint = ONL.Endpoint.ConnectToEndpoint(Address, Port, new Guid(PeerID));
                    TunnelConnectionHelper.addEndpoint(IP, newEndpoint);
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
