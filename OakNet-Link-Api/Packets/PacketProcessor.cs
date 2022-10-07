using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets.Internal;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace OakNetLink.Api.Packets
{
    public abstract class PacketProcessor 
    {
        //TODO I like singeltons, why isn't this is singleton? 
        //Key is the PacketID, value the processor type
        static Dictionary<ushort, Type> _processors = new Dictionary<ushort, Type>();
        //Key is the PacketID, value the packet type
        static Dictionary<ushort, Type> _packets = new Dictionary<ushort, Type>();

        /// <summary>
        /// This method registers a PacketProcessor which is used to
        /// process a specific packet
        /// </summary>
        /// <param name="processor">The type of the PacketProcessor</param>
        /// <param name="packet">The type of the Packet to process</param>
        internal static void addPacketProcessor(Type processor, Type packet, ushort id)
        {
            Logger.log("Registered packet with ID: " + ((id & 0xff00) >> 8) + "." + (id & 0xff));
            _packets.Add(id, packet);
            _processors.Add(id, processor);
        }

        static Type getPacketType(ushort packetID)
        {
            return _packets.Where((entry) => entry.Key == packetID).FirstOrDefault().Value;
        }

        static ushort getPacketID(Type packet)
        {
            return _packets.Where((entry) => entry.Value == packet).FirstOrDefault().Key;
        }

        /// <summary>
        /// This method encodes PacketBase objects into binary data, it also adds the PacketId  
        /// </summary>
        /// <param name="packet">PacketBase to encode</param>
        public static BinaryWriter EncodePacket(PacketBase packet)
        {
            if (packet.GetType() != typeof(PingPacket) && packet.GetType() != typeof(PongPacket))
                Logger.log("--> " + packet.GetType());
            var packetID = getPacketID(packet.GetType());
            var packetData = new BinaryWriter(new MemoryStream());
            if (packetID > ushort.MaxValue)
                throw new OverflowException("The packetID " + packetID + " is to big");
            if (packet.GetType().GetFields().Count() > 0)
                throw new Exception("Fields in packet are not supported. Use properties instead");
            packetData.Write(packetID);
            
            foreach (var propertyInfo in packet.GetType().GetProperties())
            {
                if (propertyInfo.PropertyType == typeof(byte))
                {
                    packetData.Write((byte)propertyInfo.GetValue(packet)!);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(short))
                {
                    packetData.Write((short)propertyInfo.GetValue(packet)!);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(ushort))
                {
                    packetData.Write((ushort)propertyInfo.GetValue(packet)!);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(uint))
                {
                    packetData.Write((uint)propertyInfo.GetValue(packet)!);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(int))
                {
                    packetData.Write((int)propertyInfo.GetValue(packet)!);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(char))
                {
                    packetData.Write((char)propertyInfo.GetValue(packet)!);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(string))
                {
                    packetData.Write((string)propertyInfo.GetValue(packet)!);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(byte[]))
                {
                    packetData.Write(((byte[])propertyInfo.GetValue(packet)!).Length);
                    packetData.Write((byte[])propertyInfo.GetValue(packet)!);
                    continue;
                }
                throw new NotImplementedException(propertyInfo.PropertyType.ToString() + " is not a supported Property type for a packet");
            }
            return packetData;
        }

        /// <summary>
        /// This method decodes binary data into the corresponding PacketBase Object.
        /// It also constructs the corresponding PacketProcessor and calls its processPacket method
        /// </summary>
        /// <param name="packetData">The data to decode</param>
        /// <param name="client">The client who sent this packet</param>
        /// <returns>A PacketBase which should be returned to the Sender, null if nothing shall be returned</returns>
        public static PacketBase? DecodePacket(byte[] packetData, OakNetEndPoint client)
        {
            
            var reader = new BinaryReader(new MemoryStream(packetData));

            var packetId = reader.ReadUInt16();
            var packetType = getPacketType(packetId);

            if (packetType == null) { throw new Exception("Received Unknown Packet"); }

            if (packetType != typeof(PingPacket) && packetType != typeof(PongPacket))
                Logger.log("<-- " + packetType);

            if (packetType.GetFields().Count() > 0)
            {
                throw new Exception("Fields in packet are not supported. Use properties instead");
            }

            var packet = (PacketBase)Activator.CreateInstance(packetType)!;


            foreach (var propertyInfo in packetType.GetProperties())
            {
                if (propertyInfo.PropertyType == typeof(byte))
                {
                    propertyInfo.SetValue(packet, reader.ReadByte());
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(short))
                {
                    propertyInfo.SetValue(packet, reader.ReadInt16());
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(ushort))
                {
                    propertyInfo.SetValue(packet, reader.ReadUInt16());
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(uint))
                {
                    propertyInfo.SetValue(packet, reader.ReadUInt32());
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(int))
                {
                    propertyInfo.SetValue(packet, reader.ReadInt32());
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(char))
                {
                    propertyInfo.SetValue(packet, reader.ReadChar());
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(string))
                {
                    propertyInfo.SetValue(packet, reader.ReadString());
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(byte[]))
                {
                    var length = reader.ReadInt32();
                    propertyInfo.SetValue(packet, reader.ReadBytes(length));
                    continue;
                }
            }

            Type processorType;
            if (_processors.TryGetValue(packetId, out processorType))
                // KnownPacket
                return ((PacketProcessor)Activator.CreateInstance(processorType)!).ProcessPacket(packet, client);
            return null;
        }
        /// <summary>
        /// This method needs to be overriden by each individual PacketProcessor
        /// If you want to send a packet back to the sender, your processor can return this packet here
        /// If you don't want to send a packet back, just return null
        /// </summary>
        /// <param name="packet"></param>
        /// <returns>The packet which should be sent back to the sender, null if nothing should be sent</returns>
        public abstract PacketBase ProcessPacket(PacketBase packet, OakNetEndPoint endpoint);
    }
}
