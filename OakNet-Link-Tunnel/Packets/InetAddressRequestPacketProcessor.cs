using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel.Packets
{
    public class InetAddressRequestPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var inetAddressRequestPacket = packet as InetAddressRequestPacket;
            if (inetAddressRequestPacket == null)
                return null;
            var response = new InetAddressResponsePacket();
            try 
            {
                var address = TunnelPlugin.Manager.getAddress(inetAddressRequestPacket.CanonicalName, endpoint);
                response.Address = address;
            }
            catch(Exception e)
            {
                response.ErrorMessage = e.Message;
            }

            return response;
        }
    }
}
