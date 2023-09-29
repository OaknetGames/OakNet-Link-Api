using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionCreateResponsePacket : PacketBase
    {
        public string? responseMessage { get; set; }

        public override PacketBase? ProcessPacket(OakNetEndPoint endpoint)
        {

            if (responseMessage != "Success")
            {
                SessionManager.TrialSession = null;
                SessionsPlugin.Event.SessionCreationFailed?.Invoke(responseMessage, EventArgs.Empty);
            }
            else
            {
                SessionManager.ActiveSession = SessionManager.TrialSession;
                SessionManager.ActiveSession!.CurrentPlayerCount++;
                SessionManager.TrialSession = null;
                SessionsPlugin.Event.SessionCreationSuccess?.Invoke(null, EventArgs.Empty);
            }

            return null;
        }
    }
}
