using OakNetLink;
using OakNetLink.Communication;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using TestClient;

namespace OakNet_Link_Api.Packets
{
    public class McClientDisconnectionPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            if (DummyClient.isHost)
            {
                if (DummyClient.getDummyClient(endpoint).socket == null)
                    return null;
                try
                {
                    DummyClient.getDummyClient(endpoint).socket.Shutdown(System.Net.Sockets.SocketShutdown.Both);
                    DummyClient.getDummyClient(endpoint).socket.Close();
                }
                catch (SocketException) { }
                DummyClient.getDummyClient(endpoint).socket = null;
            }
            return null;
        }
    }
}
