using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionJoinRequestPacket : Packet
    {
        public string? SessionName { get; set; }
        public string? SessionPassword { get; set; }
    }
}
