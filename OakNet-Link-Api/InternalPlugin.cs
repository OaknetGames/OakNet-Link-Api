using OakNetLink.Api.Packets;
using OakNetLink.Api.Packets.Internal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Api
{
    internal class InternalPlugin : ONLPlugin
    {
        public InternalPlugin() : base(0) {
            ONL.Event.Disconnection += (obj, args) =>
             {
                 var disconnectArgs = args as ONL.Event.DisconnectEventArgs;
                 if(disconnectArgs.endpoint == ONL.MasterServer.EndPoint)
                 {
                     Logger.log("Lost connection to MasterServer try reconnecting...");
                     disconnectArgs.endpoint.ConnectionState = Communication.ConnectionState.Connecting;
                     disconnectArgs.endpoint.tick();
                 }
             };

            ONL.Event.ConnectionFailed += (obj, args) =>
            {
                if(obj == ONL.MasterServer.EndPoint)
                {
                    Logger.log("Failed connecting to MasterServer try reconnecting...");
                    ONL.MasterServer.EndPoint.ConnectionState = Communication.ConnectionState.Connecting;
                    ONL.MasterServer.EndPoint.tick();
                }
            };
        }

   
        public override Dictionary<Type, Type> registerPackets()
        {
            var packets = new Dictionary<Type, Type>();
            packets.Add(typeof(PingPacket), typeof(PingPacketProcessor));
            packets.Add(typeof(PongPacket), typeof(PongPacketProcessor));
            packets.Add(typeof(ConnectionRequestPacket), typeof(ConnectionRequestPacketProcessor));
            packets.Add(typeof(ConnectionEstablishedPacket), typeof(ConnectionEstablishedPacketProcessor));
            packets.Add(typeof(DisconnectPacket), typeof(DisconnectPacketProcessor));
            packets.Add(typeof(LargePacketPacket), typeof(LargePacketPacketProcessor));
            return packets;
        }
    }
}
