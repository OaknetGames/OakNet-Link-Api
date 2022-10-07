using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionJoinRequestResponsePacket : PacketBase
    {
        public string? ResponseMessage { get; set; }
        public byte[]? endpointsData { get; set; }

        /***
         * int32 playerCount
         * For Each Player
         *   4 Bytes IPAddressBytes 
         *   int Port
         *   16 Bytes GUID
         * **/
    }
}
