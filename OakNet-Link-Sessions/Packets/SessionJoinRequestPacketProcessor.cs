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
        public override Packet? processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var sessionJoinRequestPacket = packet as SessionJoinRequestPacket;
            if (sessionJoinRequestPacket == null)
                return null;

            var session = SessionManager.AvailableSessions.Where((s) => s.Name == sessionJoinRequestPacket.SessionName).FirstOrDefault();
            
            if (session == null)
                return new SessionJoinRequestResponsePacket() { ResponseMessage = "SessionNotFound" };

            if(session.Password != sessionJoinRequestPacket.SessionPassword)
                return new SessionJoinRequestResponsePacket() { ResponseMessage = "WrongPassword" };

            var peers = session.OakNetEndPoints.FirstOrDefault().IpAddress + ":" + session.OakNetEndPoints.FirstOrDefault().Port;
            foreach (var sessionPeer in session.OakNetEndPoints.Skip(1))
            {
                peers += ";" + sessionPeer.IpAddress + ":" + sessionPeer.Port;
            }

            foreach (var sessionPeer in session.OakNetEndPoints)
            {
                var memberJoinedPacket = new SessionMemberConnectedPacket() { ConnectedMember = $"{endpoint.IpAddress.ToString()}:{endpoint.Port}" };
                Communicator.instance.sendPacket(PacketProcessor.encodePacket(memberJoinedPacket), sessionPeer, false, true, false);
            }
            session.oakNetEndPoints.Add(endpoint);
            return new SessionJoinRequestResponsePacket() { ResponseMessage = "Success", Endpoints = peers };
        }
    }
}
