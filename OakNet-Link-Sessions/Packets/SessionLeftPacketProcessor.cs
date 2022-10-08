using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using OakNetLink.Sessions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNet_Link_Sessions.Packets
{
    public class SessionLeftPacketProcessor : PacketProcessor
    {
        public override PacketBase ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            SessionManager.EndPointLeft(endpoint);
            return null;
        }
    }
}
