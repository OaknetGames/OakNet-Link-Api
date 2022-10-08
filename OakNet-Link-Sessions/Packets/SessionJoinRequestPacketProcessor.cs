using OakNetLink.Api.Communication;
using System;
using System.Linq;
using System.Collections.Generic;
using System.Text;
using OakNetLink.Api.Packets;

namespace OakNetLink.Sessions.Packets
{
    internal class SessionJoinRequestPacketProcessor : PacketProcessor
    {
        public override PacketBase? ProcessPacket(PacketBase packet, OakNetEndPoint endpoint)
        {
            var sessionJoinRequestPacket = packet as SessionJoinRequestPacket;
            if (sessionJoinRequestPacket == null)
                return null;

            var session = SessionManager.AvailableSessions.Where((s) => s.Name == sessionJoinRequestPacket.SessionName).FirstOrDefault();
            
            if (session == null)
                return new SessionJoinRequestResponsePacket() { ResponseMessage = "SessionNotFound" };

            if(session.MaxPlayers == session.CurrentPlayerCount)
                return new SessionJoinRequestResponsePacket() { ResponseMessage = "SessionFull" };


            if (session.Password != sessionJoinRequestPacket.SessionPassword)
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
                
                var memberJoinedPacket = new SessionMemberConnectedPacket() { memberData=((MemoryStream) writerMemberJoinedPacket.BaseStream).ToArray() };
                Communicator.instance.sendPacket(PacketProcessor.EncodePacket(memberJoinedPacket), sessionPeer, false, true, false);
            }
            session.oakNetEndPoints.Add(endpoint);
            session.CurrentPlayerCount++;
            return new SessionJoinRequestResponsePacket() { ResponseMessage = "Success", endpointsData = ((MemoryStream)writerSessionJoinRequestResponsePacket.BaseStream).ToArray() };
        }
    }
}
