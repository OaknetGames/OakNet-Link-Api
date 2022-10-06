﻿using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionJoinRequestResponsePacket : PacketBase
    {
        public string? ResponseMessage { get; set; }
        public string? Endpoints { get; set; }
    }
}
