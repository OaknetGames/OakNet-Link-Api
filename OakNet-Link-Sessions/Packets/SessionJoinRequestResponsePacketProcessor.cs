using OakNetLink.Api.Communication;
using System;
using System.Linq;
using System.Collections.Generic;
using System.Net;
using System.Text;
using OakNetLink.Api.Packets;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionJoinRequestResponsePacketProcessor : PacketProcessor
    {
        public override PacketBase? ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            var sessionJoinRequestResponsePacket = packet as SessionJoinRequestResponsePacket;
            if(sessionJoinRequestResponsePacket == null)
                return null;
            if(sessionJoinRequestResponsePacket.ResponseMessage != "Success")
            {
                SessionsPlugin.Event.SessionJoinDenied?.Invoke(sessionJoinRequestResponsePacket?.ResponseMessage, EventArgs.Empty);
                return null;
            }

            SessionManager.ActiveSession = SessionManager.TrialSession;
            SessionManager.TrialSession = null;

            var endpoints = sessionJoinRequestResponsePacket!.endpointsData!;
            var reader = new BinaryReader(new MemoryStream(endpoints));
            var count = reader.ReadInt32();
            for (int i = 0; i < count; i++)
            {
                var address = new IPAddress(reader.ReadBytes(4));
                var port = reader.ReadInt32();
                var guid = new Guid(reader.ReadBytes(16));

                var newEndpoint = OakNetEndPointManager.Notify(address, port, guid);
                newEndpoint.ConnectionState = ConnectionState.Connecting;
                newEndpoint.tick();
                SessionManager.ActiveSession!.oakNetEndPoints.Add(newEndpoint);
            }
            SessionsPlugin.Event.SessionJoinSuccess?.Invoke(null, EventArgs.Empty);
            return null;
        }
    }
}
