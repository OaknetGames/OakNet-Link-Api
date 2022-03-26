using OakNetLink.Api;
using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    class SessionCreatePacketProcessor : PacketProcessor
    {
        public override Packet? processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var sessionCreatePacket = packet as SessionCreatePacket;
            if (sessionCreatePacket == null)
                return null;
            if (SessionManager.CreateNewSession(sessionCreatePacket.SessionName, sessionCreatePacket.SessionPassword, endpoint))
            {
                Logger.log(endpoint.IpAddress.ToString() + " created new Session: " + sessionCreatePacket.SessionName);
                return new SessionCreateResponsePacket() { responseMessage = "Success" };
            }
            else
            {
                Logger.log(endpoint.IpAddress.ToString() + " tried creating a new Session: " + sessionCreatePacket.SessionName + " but it already existed.");
                return new SessionCreateResponsePacket() { responseMessage = "SessionAlreadyExists" };
            }
        }
    }
}
