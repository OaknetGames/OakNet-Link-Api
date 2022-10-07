using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionCreatePacket : PacketBase
    {
        public string SessionName { get; set; } = "";
        public string SessionPassword { get; set; } = "";
        public byte[] Payload { get; set; } = new byte[0];

    }
}
