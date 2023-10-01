using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChatApp_Test.Packets
{
    internal class MessagePacket : PacketBase
    {
        public string? Message { get; set; }

        public override PacketBase? ProcessPacket(OakNetEndPoint peer)
        {
            Console.WriteLine($"{peer.IpAddress} said: {Message}");
            return null;
        }
    }
}
