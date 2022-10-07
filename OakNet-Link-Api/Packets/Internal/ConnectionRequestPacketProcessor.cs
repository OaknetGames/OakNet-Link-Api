using OakNetLink.Api.Communication;
using System;

namespace OakNetLink.Api.Packets.Internal
{
    internal class ConnectionRequestPacketProcessor : PacketProcessor
    {
        public override PacketBase ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            var request = packet as ConnectionRequestPacket;
            
            if (request == null) return null;

            var response = new ConnectionEstablishedPacket();
            if(ONL.Event.ConnectionRequest != null)
            {
                var args = new ONL.Event.ConnectionRequestEventArgs(endpoint.PeerID);
                ONL.Event.ConnectionRequest.Invoke(endpoint, args);
                if (!args.accepted)
                    return null;
            }
            if (Communicator.instance.isServer)
            {
                endpoint.ConnectionState = ConnectionState.Connected;
            }
            return response;
        }
    }
}
