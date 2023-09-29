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
    public class InetAddressResponsePacket : PacketBase
    {
        public int Address { get; set; }
        public string ErrorMessage { get; set; }
        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            if (Address == 0)
            {
                Logger.log("Got an error whilst requesting the IP address: " + ErrorMessage);
                return null;
            }
            Logger.log("Received IP: " + Address);
            TunDriverWrapper.createAdapter(Address);
            return null;
        }
    }
}
