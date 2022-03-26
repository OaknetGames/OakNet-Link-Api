using OakNetLink.Api.Communication;

namespace OakNetLink.Api.Packets.Internal
{
    internal class ConnectionRequestPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var response = new ConnectionEstablishedPacket();
            if (Communicator.instance.isServer)
            {
                endpoint.ConnectionState = ConnectionState.Connected;
            }
            return response;
        }
    }
}
