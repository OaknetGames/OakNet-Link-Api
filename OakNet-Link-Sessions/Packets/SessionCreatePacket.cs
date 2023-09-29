using OakNetLink.Api.Communication;
using OakNetLink.Api;
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
        public int MaxPlayers { get; set; } 
        public byte[] Payload { get; set; } = new byte[0];

        public override PacketBase? ProcessPacket(OakNetEndPoint endpoint)
        {
            if (SessionManager.CreateNewSession(SessionName, SessionPassword, MaxPlayers, Payload, endpoint))
            {
                Logger.log(endpoint.PeerID + " created new Session: " + SessionName);
                return new SessionCreateResponsePacket() { responseMessage = "Success" };
            }
            else
            {
                Logger.log(endpoint.PeerID + " tried creating a new Session: " + SessionName + " but it already existed.");
                return new SessionCreateResponsePacket() { responseMessage = "SessionAlreadyExists" };
            }
        }

    }
}
