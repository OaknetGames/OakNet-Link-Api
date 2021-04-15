using OakNetLink;
using OakNetLink.Communication;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestClient.Packets
{
    public class MessagePacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            MessagePacket mpacket = packet as MessagePacket;
            Console.WriteLine(mpacket.name + ": " + mpacket.msg);
            return null;
        }
    }
}
