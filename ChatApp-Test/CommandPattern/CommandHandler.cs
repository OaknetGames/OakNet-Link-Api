using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChatApp_Test.CommandPattern
{
    internal class CommandHandler
    {

        private Dictionary<string, Command> commands;
        private bool running = false;

        public CommandHandler() {
            commands = new Dictionary<string, Command>();
            initCommands();
            running = true;
        }

        public void handleUserInput()
        {
            while (running)
            {
                // send-message | some message to be sent
                var line = Console.ReadLine();
                string commandName = line.Split(" ")[0];
                string message = string.Concat(line.SkipWhile(c => c != ' ').Skip(1));
                string[] args = { message };

                
                if (commands.ContainsKey(commandName))
                    commands[commandName].Execute(args);
                else
                    Console.WriteLine("Dont know that command");
            }
        }

        private void initCommands()
        {
            addCommand(new SendMessageCommand(this));
            addCommand(new JoinSessionCommand(this));
            addCommand(new CreateSessionCommand(this));
            addCommand(new RefreshCommand(this));
            addCommand(new ListSessionsCommand(this));
        }

        private void addCommand(Command command)
        {
            this.commands.Add(command.Name, command);
        }

        public void quitProgram()
        {
            running = false;
        }
    }
}
