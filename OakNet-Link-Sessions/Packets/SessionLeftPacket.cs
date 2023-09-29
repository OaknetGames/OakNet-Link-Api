using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Sessions.Packets
{
    public class SessionLeftPacket : PacketBase
    {
        public override PacketBase? ProcessPacket(OakNetEndPoint endpoint)
        {
            SessionManager.EndPointLeft(endpoint);
            return null;
        }
    }
}
