using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets.Internal
{
    internal class DisconnectPacket : Packet
    {
        public string Message { get; set; }
    }
}
