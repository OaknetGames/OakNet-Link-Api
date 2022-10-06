﻿
using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Net;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    class SessionCreateResponsePacketProcessor : PacketProcessor
    {
        public override PacketBase? ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            var sessionCreateResponsePacket = packet as SessionCreateResponsePacket;

            if(sessionCreateResponsePacket?.responseMessage != "Success")
                SessionsPlugin.Event.SessionCreationFailed?.Invoke(sessionCreateResponsePacket?.responseMessage, EventArgs.Empty);
            else
                SessionsPlugin.Event.SessionCreationSuccess?.Invoke(null, EventArgs.Empty);

            return null;
        }
    }
}