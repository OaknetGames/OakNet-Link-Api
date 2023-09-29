using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionJoinRequestPacket : PacketBase
    {
        public string? SessionName { get; set; }
        public string? SessionPassword { get; set; }

        public override PacketBase? ProcessPacket(OakNetEndPoint endpoint)
        {
            var session = SessionManager.AvailableSessions.Where((s) => s.Name == SessionName).FirstOrDefault();

            if (session == null)
                return new SessionJoinRequestResponsePacket() { ResponseMessage = "SessionNotFound" };

            if (session.MaxPlayers == session.CurrentPlayerCount)
                return new SessionJoinRequestResponsePacket() { ResponseMessage = "SessionFull" };


            if (session.Password != SessionPassword)
                return new SessionJoinRequestResponsePacket() { ResponseMessage = "WrongPassword" };

            var writerSessionJoinRequestResponsePacket = new BinaryWriter(new MemoryStream());
            writerSessionJoinRequestResponsePacket.Write(session.OakNetEndPoints.Count());
            foreach (var sessionPeer in session.OakNetEndPoints)
            {
                writerSessionJoinRequestResponsePacket.Write(sessionPeer.IpAddress.GetAddressBytes());
                writerSessionJoinRequestResponsePacket.Write(sessionPeer.Port);
                writerSessionJoinRequestResponsePacket.Write(sessionPeer.PeerID.ToByteArray());

            }
            var writerMemberJoinedPacket = new BinaryWriter(new MemoryStream());
            writerMemberJoinedPacket.Write(endpoint.IpAddress.GetAddressBytes());
            writerMemberJoinedPacket.Write(endpoint.Port);
            writerMemberJoinedPacket.Write(endpoint.PeerID.ToByteArray());

            foreach (var sessionPeer in session.OakNetEndPoints)
            {

                var memberJoinedPacket = new SessionMemberConnectedPacket() { memberData = ((MemoryStream)writerMemberJoinedPacket.BaseStream).ToArray() };
                Communicator.instance.sendPacket(PacketProcessor.EncodePacket(memberJoinedPacket), sessionPeer, false, true, false);
            }
            session.oakNetEndPoints.Add(endpoint);
            session.CurrentPlayerCount++;
            return new SessionJoinRequestResponsePacket() { ResponseMessage = "Success", endpointsData = ((MemoryStream)writerSessionJoinRequestResponsePacket.BaseStream).ToArray() };
        }
    }
}
