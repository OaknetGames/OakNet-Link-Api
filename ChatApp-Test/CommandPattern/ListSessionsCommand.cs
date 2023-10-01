using OakNetLink.Sessions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChatApp_Test.CommandPattern
{
    internal class ListSessionsCommand : Command
    {

        public ListSessionsCommand(CommandHandler handler) :base("list-sessions", handler) { 
        
        }

        public override void Execute(string[] args)
        {
            List<Session> sessions = SessionsPlugin.AvailableSessions();

            foreach(Session session in sessions)
            {
                Console.WriteLine(session.Name);
            }
        }
    }
}
