using OakNetLink;
using OakNetLink.Communication;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestClient.Packets
{
    public class McDataServerClientPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            McDataServerClientPacket mdscp = packet as McDataServerClientPacket;
            DummyServer.sendData(mdscp.data);
            return null;
        }
    }
}
