using OakNetLink.Sessions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChatApp_Test.CommandPattern
{
    internal class RefreshCommand : Command
    {

        public RefreshCommand(CommandHandler handler) :base("refresh", handler) { 
        
        }

        public override void Execute(string[] args)
        {
            SessionsPlugin.FetchSessions();

            Console.WriteLine("Refresh successful!");
        }
    }
}
