using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    internal class StrangerTunnelConnectionResponsePacket : PacketBase
    {
        public string Address { get; set; }
        public int Port { get; set; }
        public string Response { get; set; }
        public byte[] PeerID { get; set; }

        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            var receipient = OakNetEndPointManager.GetEndPoint(new Guid(PeerID));
            if (receipient == null) return null;

            var newResponse = new TunnelConnectionResponsePacket();
            if (Response == "accepted")
            {
                newResponse.Address = endpoint.IpAddress.ToString();
                newResponse.Port = endpoint.Port;
                newResponse.PeerID = endpoint.PeerID.ToByteArray();
                newResponse.Message = "accepted";
            }
            newResponse.IP = TunnelPlugin.Manager.getAddress(endpoint);
            newResponse.Message = Response;
            receipient.sendPacket(newResponse, true);

            return null;
        }
    }
}
