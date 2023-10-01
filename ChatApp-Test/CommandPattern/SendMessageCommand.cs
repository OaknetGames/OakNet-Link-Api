using ChatApp_Test.Packets;
using OakNetLink.Sessions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChatApp_Test.CommandPattern
{
    internal class SendMessageCommand : Command
    {

        private static string Name = "send-message";

        public SendMessageCommand(CommandHandler handler) : base(Name, handler) {
        }


        public override void Execute(string[] args)
        {
            MessagePacket packet = new MessagePacket();
            packet.Message = args[0];

            SessionsPlugin.SendBroadcast(packet, true);

            Console.WriteLine("Sending message... \"" + args[0] + "\"");
        }
    }
}
