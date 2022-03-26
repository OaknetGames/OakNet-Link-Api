using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionCreateResponsePacket : Packet
    {
        public string? responseMessage { get; set; }
    }
}
