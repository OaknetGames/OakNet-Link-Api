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
        public override Packet? processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var sessionJoinRequestResponsePacket = packet as SessionJoinRequestResponsePacket;
            if(sessionJoinRequestResponsePacket == null)
                return null;
            if(sessionJoinRequestResponsePacket.ResponseMessage != "Success")
            {
                Sessions.Event.SessionJoinDenied?.Invoke(sessionJoinRequestResponsePacket?.ResponseMessage, EventArgs.Empty);
                return null;
            }
           
            var endpoints = sessionJoinRequestResponsePacket?.Endpoints?.Split(';');
            if(endpoints!= null)
                foreach(var endpointData in endpoints)
                {
                    var ipAddress = endpointData.Substring(0, endpointData.LastIndexOf(":"));
                    var port = endpointData.Split(':').Last();
                    var newEndpoint = OakNetEndPointManager.Notify(IPAddress.Parse(ipAddress), Convert.ToInt32(port));
                    newEndpoint.ConnectionState = ConnectionState.Connecting;
                    newEndpoint.tick();
                }
            Sessions.Event.SessionJoinSuccess?.Invoke(null, EventArgs.Empty);
            return null;
        }
    }
}
