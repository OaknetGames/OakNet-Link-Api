using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets.Internal
{
    internal class DisconnectPacket : PacketBase
    {
        public string Message { get; set; }

        public override PacketBase ProcessPacket(OakNetEndPoint endpoint)
        {
            endpoint.Disconnect(Message);
            return null;
        }
    }
}
