using OakNetLink;
using OakNetLink.Communication;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestClient.Packets
{
    public class McDataClientServerPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            McDataClientServerPacket mdcs = packet as McDataClientServerPacket;
            if(DummyClient.isHost)
                DummyClient.getDummyClient(endpoint).sendData(mdcs.data);
            return null;
        }
    }
}
