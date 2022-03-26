using OakNetLink.Api.Communication;

namespace OakNetLink.Api.Packets.Internal
{
    internal class ConnectionEstablishedPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            endpoint.ConnectionState = ConnectionState.Connected;
            endpoint.tick();
            ONL.Event.ConnectionEstablished.Invoke(endpoint, null);
            return null;
        }
    }
}
