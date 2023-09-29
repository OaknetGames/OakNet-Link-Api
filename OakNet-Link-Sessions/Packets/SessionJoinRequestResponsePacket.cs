using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Net;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionJoinRequestResponsePacket : PacketBase
    {
        public string ResponseMessage { get; set; } = "";
        public byte[] endpointsData { get; set; } = new byte[0];

        /***
         * int32 playerCount
         * For Each Player
         *   4 Bytes IPAddressBytes 
         *   int Port
         *   16 Bytes GUID
         * **/

        public override PacketBase? ProcessPacket(OakNetEndPoint endpoint)
        {
            if (ResponseMessage != "Success")
            {
                SessionsPlugin.Event.SessionJoinDenied?.Invoke(ResponseMessage, EventArgs.Empty);
                return null;
            }

            SessionManager.ActiveSession = SessionManager.TrialSession;
            SessionManager.TrialSession = null;

            var endpoints = endpointsData!;
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
            SessionManager.ActiveSession!.CurrentPlayerCount = count + 1;
            SessionsPlugin.Event.SessionJoinSuccess?.Invoke(null, EventArgs.Empty);
            return null;
        }
    }
}
