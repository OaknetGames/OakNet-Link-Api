using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets
{
    /// <summary>
    /// This is the base class of all Packets
    /// </summary>
    public abstract class PacketBase
    {
        /// <summary>
        /// This method needs to be overriden by each individual PacketProcessor
        /// If you want to send a packet back to the sender, your processor can return this packet here
        /// If you don't want to send a packet back, just return null
        /// </summary>
        /// <param name="packet"></param>
        /// <returns>The packet which should be sent back to the sender, null if nothing should be sent</returns>
        public abstract PacketBase ProcessPacket(OakNetEndPoint peer);
    }
}
