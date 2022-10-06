using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets.Internal
{
    internal class DisconnectPacketProcessor : PacketProcessor
    {
        public override PacketBase ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            var disconnectPacket = packet as DisconnectPacket;
            Communicator.instance.disconnect(endpoint, disconnectPacket.Message);
            return null;
        }
    }
}
