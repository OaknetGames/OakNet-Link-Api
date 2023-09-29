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
    internal class TunnelConnectionResponsePacket : PacketBase
    {
        public string Address { get; set; }
        public int IP { get; set; }
        public int Port { get; set; }
        public byte[] PeerID { get; set; }
        public string Message { get; set; }

        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            if (Message == "accepted")
            {
                Logger.log("other Endpoint accepted the connection!");
                var newEndpoint = ONL.Endpoint.ConnectToEndpoint(Address, Port, new Guid(PeerID));
                TunnelConnectionHelper.addEndpoint(IP, newEndpoint);
            }
            else
            {
                Logger.log("other Endpoint rejected the connection: " + Message);
                TunnelConnectionHelper.removeAddress(IP);
            }

            return null;
        }
    }
}
