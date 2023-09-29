using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    public class InetAddressRequestPacket : PacketBase
    {
        public string CanonicalName { get; set; }

        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            var response = new InetAddressResponsePacket();
            try
            {
                var address = TunnelPlugin.Manager.getAddress(CanonicalName, endpoint);
                response.Address = address;
            }
            catch (Exception e)
            {
                response.ErrorMessage = e.Message;
            }

            return response;
        }
    }
}
