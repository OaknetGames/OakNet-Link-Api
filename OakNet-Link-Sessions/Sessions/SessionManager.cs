using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets.Internal;
using OakNetLink.Api.Packets;
using OakNetLink.Sessions.Packets;
using OakNetLink.Api;
using static System.Collections.Specialized.BitVector32;

namespace OakNetLink.Sessions
{
    internal class SessionManager
    {
        public static List<Session> AvailableSessions { get { return sessions; } }

        static List<Session> sessions = new List<Session>();

        public static Session? TrialSession { get; set; }
        public static Session? ActiveSession { get; set; }

        public static void FetchSessions()
        {
            Communicator.instance.sendPacket(PacketProcessor.EncodePacket(new SessionFetchListPacket()), OakNetEndPointManager.MasterServerEndpoint, false, true, false);
         }

        public static bool CreateNewSession(string name, string password, int maxPlayers, byte[]? payload, OakNetEndPoint creator)
        {
            if (sessions.Any((session) => session.Name == name))
                return false;

            var newSession = new Session() { Name = name, Password = password, MaxPlayers = maxPlayers, HasPassword = password != "",  Payload = payload };
            newSession.oakNetEndPoints.Add(creator);
            sessions.Add(newSession);
            return true;
        }
        
        public static void EndPointLeft(OakNetEndPoint endPoint)
        {
            if (ActiveSession!= null && ActiveSession.oakNetEndPoints.Contains(endPoint))
            {
                ActiveSession.oakNetEndPoints.Remove(endPoint);
                ActiveSession.CurrentPlayerCount--;
                return;
            }

            foreach(var session in sessions.ToList())
            {
                if (session.oakNetEndPoints.Contains(endPoint))
                    session.oakNetEndPoints.Remove(endPoint);
                
                if(session.oakNetEndPoints.Count == 0)
                {
                    sessions.Remove(session);
                    Logger.log($"Session: {session.Name} is empty, removing...");
                }
            }
        }
    }
}
