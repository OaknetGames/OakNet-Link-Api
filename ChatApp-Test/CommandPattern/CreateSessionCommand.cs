using OakNetLink.Sessions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChatApp_Test.CommandPattern
{
    internal class CreateSessionCommand : Command
    {

        public CreateSessionCommand(CommandHandler handler) :base("create-session", handler) { }

        public override void Execute(string[] args)
        {
            // args will be structured as follows:
            // name + password + numPlayers + payload

            //SessionsPlugin.CreateNewSession(args[0], args[1], args[2], args[3]);
            SessionsPlugin.CreateNewSession(args[0], "", 4, new byte[0]);

            Console.WriteLine("Created session with id: \"" + args[0] + "\"");
        }
    }
}
