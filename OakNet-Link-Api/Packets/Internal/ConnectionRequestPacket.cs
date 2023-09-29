using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets.Internal
{
    internal class ConnectionRequestPacket : PacketBase
    {
        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {

            var response = new ConnectionEstablishedPacket();
            if (ONL.Event.ConnectionRequest != null)
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
