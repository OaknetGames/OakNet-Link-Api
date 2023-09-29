using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets.Internal
{
    class ConnectionEstablishedPacket : PacketBase
    {
        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            endpoint.ConnectionState = ConnectionState.Connected;
            endpoint.tick();
            ONL.Event.ConnectionEstablished?.Invoke(endpoint, null);
            return null;
        }
    }
}
