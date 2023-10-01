using OakNetLink.Sessions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChatApp_Test.CommandPattern
{
    internal class JoinSessionCommand : Command
    {

        public JoinSessionCommand(CommandHandler handler) :base("join-session", handler)
        {

        }

        public override void Execute(string[] args)
        {
            List<Session> sessions = SessionsPlugin.AvailableSessions();

            Session targetSession = null;

            foreach (Session session in sessions)
            {
                if (session.Name.Equals(args[0]))
                {
                    targetSession = session;
                }
            }

            SessionsPlugin.JoinSession(targetSession, "");
        }
    }
}
