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
        public override PacketBase? ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            var sessionCreatePacket = packet as SessionCreatePacket;
            if (sessionCreatePacket == null)
                return null;
            if (SessionManager.CreateNewSession(sessionCreatePacket.SessionName, sessionCreatePacket.SessionPassword, sessionCreatePacket.MaxPlayers, sessionCreatePacket.Payload, endpoint))
            {
                Logger.log(endpoint.PeerID + " created new Session: " + sessionCreatePacket.SessionName);
                return new SessionCreateResponsePacket() { responseMessage = "Success" };
            }
            else
            {
                Logger.log(endpoint.PeerID + " tried creating a new Session: " + sessionCreatePacket.SessionName + " but it already existed.");
                return new SessionCreateResponsePacket() { responseMessage = "SessionAlreadyExists" };
            }
        }
    }
}
