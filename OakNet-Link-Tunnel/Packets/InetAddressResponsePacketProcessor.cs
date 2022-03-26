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
    public class InetAddressResponsePacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var responsepacket = packet as InetAddressResponsePacket;
            if(responsepacket == null)
                return null;
            if (responsepacket.Address  == 0)
            {
                Logger.log("Got an error whilst requesting the IP address: " + responsepacket.ErrorMessage);
                return null;
            }
            Logger.log("Received IP: " + responsepacket.Address);
            TunDriverWrapper.createAdapter(responsepacket.Address);
            return null;
        }
    }
}
