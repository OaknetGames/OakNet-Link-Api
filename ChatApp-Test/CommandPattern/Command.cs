using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChatApp_Test.CommandPattern
{
    internal abstract class Command
    {

        internal string Name { get; private set; }
        private CommandHandler handler;

        protected Command(string name, CommandHandler handler) {
            this.Name = name;
            this.handler = handler;
        }

        public abstract void Execute(string[] args);
    }
}
