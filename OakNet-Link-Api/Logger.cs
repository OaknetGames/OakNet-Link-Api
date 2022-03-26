using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api
{
    public class Logger
    {

        public static void log(string message)
        {
            ONL.Event.Log.Invoke(null, new ONL.Event.LogEventArgs(message));
        }
    }
}
