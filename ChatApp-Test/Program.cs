using ChatApp_Test;
using ChatApp_Test.CommandPattern;
using OakNetLink.Api;
using OakNetLink.Api.Communication;
using OakNetLink.Sessions;
using System;

// Package
namespace MyApp // Note: actual namespace depends on the project name.
{
    // Class
    internal class Program
    {
        // Method
        static void Main(string[] args)
        {
            ONL.Event.Log += (obj, args) => Console.WriteLine((args as ONL.Event.LogEventArgs).message);
            ONL.Event.ConnectionFailed += (obj, args) => Console.WriteLine("ConnectionFailed!");
            ONL.Event.Disconnection += (obj, args) => Console.WriteLine((args as ONL.Event.DisconnectEventArgs).endpoint.IpAddress.ToString() + ":" + (args as ONL.Event.DisconnectEventArgs).endpoint.Port + " disconnected: " + (args as ONL.Event.DisconnectEventArgs).reason);
            ONL.Event.ConnectionEstablished += (obj, args) => Console.WriteLine((obj as OakNetEndPoint).IpAddress + ":" + (obj as OakNetEndPoint).Port + " Connected!");
            ONL.Event.ConnectionLost += (obj, args) => Console.WriteLine("LostConnection!");
            ONL.Event.ConnectionRequest += (obj, args) => { Console.WriteLine((obj as OakNetEndPoint).PeerID + " requested Connection"); (args as ONL.Event.ConnectionRequestEventArgs).accepted = true; };

            SessionsPlugin.Event.SessionJoinSuccess += (obj, args) => Console.WriteLine("Joined successfully");
            SessionsPlugin.Event.SessionJoinDenied += (obj, args) => Console.WriteLine("Could not join. Error: " + obj);
            SessionsPlugin.Event.SessionListUpdated += (obj, args) => Console.WriteLine("Updated list");
            SessionsPlugin.Event.SessionCreationSuccess += (obj, args) => Console.WriteLine("Successfully created session");
            SessionsPlugin.Event.SessionCreationFailed += (obj, args) => Console.WriteLine("Could not create session. Error: " + obj);

            ONL.registerPlugin(new SessionsPlugin());
            ONL.registerPlugin(new ChatPlugin());

            ONL.MasterServer.Connect("127.0.0.1");

            // TODO: Implement as singleton
            CommandHandler handler = new CommandHandler();
            handler.handleUserInput();
        }
    }
}
