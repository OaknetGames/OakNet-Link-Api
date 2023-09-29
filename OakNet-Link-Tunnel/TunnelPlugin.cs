using OakNetLink.Api;
using OakNetLink.Tunnel.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel
{
    public class TunnelPlugin : ONLPlugin
    {
        // Only used on MasterServer side
        public static AbstractAddressManager Manager { get; set; }

        public static void requestIpAddress(string canonicalName)
        {
            var packet = new InetAddressRequestPacket();
            packet.CanonicalName = canonicalName;
            ONL.MasterServer.SendPacket(packet, true);
        }

        public TunnelPlugin() : base(2)
        {
            TunDriverWrapper.setOnPacketReceived(onPacketReceived);
        }

        void onPacketReceived(byte[] packetData)
        {
            var version = (packetData[0] & 0xf0) >> 4;
            // Filter IP version 4 
            if (version != 4)
                return;
            // Retrieve the destination Address
            var destAddress = (packetData[16]<<24) | (packetData[17]<<16) | (packetData[18]<<8) | (packetData[19]);
            // Retrive the destination network Address
            var destNetwork = destAddress & 0xffff0000; // 255.255.0.0 Subnet Mask
            // Filter the network
            if (destNetwork != 0x0a060000) //Network address 10.6.0.0
                return; // wrong network
            // handle loopback
            if(destAddress == TunDriverWrapper.CurrentAddress) // Loopback
            {
                TunDriverWrapper.sendPacketData(packetData);
            }
            //Ignore broadcasts
            if (destAddress == 0x0a06ffff) 
                return;
            //Finaly process packet
            //Logger.log($"Got Packet for: {(destAddress & 0xff000000) >> 24}.{(destAddress & 0x00ff0000) >> 16}.{(destAddress & 0x0000ff00) >> 8}.{(destAddress & 0x000000ff)}");
            var peer = TunnelConnectionHelper.getEndpointForAddress(destAddress);
            if (peer == null)
                return;
            var IPPacket = new IPPacketDataPacket();
            IPPacket.Data = packetData;
            peer.sendPacket(IPPacket, false);
        }

        public override List<Type> registerPackets() => new List<Type> {
            typeof(InetAddressRequestPacket),
            typeof(InetAddressResponsePacket),
            typeof(TunnelConnectionRequestPacket),
            typeof(TunnelConnectionResponsePacket),
            typeof(StrangerTunnelConnectionRequestPacket),
            typeof(StrangerTunnelConnectionResponsePacket),
            typeof(IPPacketDataPacket)
        };
    }
    
}
