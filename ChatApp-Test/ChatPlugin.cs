using ChatApp_Test.Packets;
using OakNetLink.Api;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChatApp_Test
{
    internal class ChatPlugin : ONLPlugin
    {
        public ChatPlugin() : base(3)
        {

        }

        public override List<Type> registerPackets()
        {
            return new List<Type>() {
                typeof (MessagePacket)
            };
        }
    }
}
