﻿using OakNetLink;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestClient.Packets
{
    public class McDataClientServerPacket : Packet
    {
        public byte[] data { get; set; }
    }
}
