using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionListResponsePacket : PacketBase
    {

        public byte[] SessionData { get; set; }
    }
}
