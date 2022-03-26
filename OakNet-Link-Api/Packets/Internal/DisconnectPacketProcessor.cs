using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets.Internal
{
    internal class DisconnectPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var disconnectPacket = packet as DisconnectPacket;
            Communicator.instance.disconnect(endpoint, disconnectPacket.Message);
            return null;
        }
    }
}
