using OakNetLink.Api.Communication;
using OakNetLink.Api.Packets.Internal;
using System;
using System.Collections.Generic;
using System.Linq;

namespace OakNetLink.Api.Packets
{
    public abstract class PacketProcessor 
    {
        //TODO I like singeltons, why isn't this is singleton? 
        //Key is the PacketID, value the processor type
        static Dictionary<int, Type> _processors = new Dictionary<int, Type>();
        //Key is the PacketID, value the packet type
        static Dictionary<int, Type> _packets = new Dictionary<int, Type>();

        /// <summary>
        /// This method registers a PacketProcessor which is used to
        /// process a specific packet
        /// </summary>
        /// <param name="processor">The type of the PacketProcessor</param>
        /// <param name="packet">The type of the Packet to process</param>
        internal static void addPacketProcessor(Type processor, Type packet, int id)
        {
            Logger.log("Registered packet with ID: " + ((id & 0xff00) >> 8) + "." + (id & 0xff));
            _packets.Add(id, packet);
            _processors.Add(id, processor);
        }

        static Type getPacketType(int packetID)
        {
            return _packets.Where((entry) => entry.Key == packetID).FirstOrDefault().Value;
        }

        static int getPacketID(Type packet)
        {
            return _packets.Where((entry) => entry.Value == packet).FirstOrDefault().Key;
        }

        public static byte[] encodePacket(Packet packet)
        {
            //if (packet.GetType() != typeof(PingPacket) && packet.GetType() != typeof(PongPacket))
            //    Logger.log("--> " + packet.GetType());
            var packetID = getPacketID(packet.GetType());
            var packetData = new byte[0];
            if (packetID > ushort.MaxValue)
                throw new OverflowException("The packetID " + packetID + " is to big");
            PacketEncoder.EncodeUShort((ushort) packetID, ref packetData);
            foreach (var fieldInfo in packet.GetType().GetFields())
            {
                if (fieldInfo.FieldType == typeof(short))
                {
                    PacketEncoder.EncodeShort((short)fieldInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (fieldInfo.FieldType == typeof(ushort))
                {
                    PacketEncoder.EncodeUShort((ushort)fieldInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (fieldInfo.FieldType == typeof(int))
                {
                    PacketEncoder.EncodeInt((int)fieldInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (fieldInfo.FieldType == typeof(char))
                {
                    PacketEncoder.EncodeChar((char)fieldInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (fieldInfo.FieldType == typeof(string))
                {
                    PacketEncoder.EncodeString((string)fieldInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (fieldInfo.FieldType == typeof(byte[]))
                {
                    PacketEncoder.EncodeByteArray((byte[])fieldInfo.GetValue(packet), ref packetData);
                    continue;
                }
                throw new NotImplementedException(fieldInfo.FieldType.ToString() + " is not a supported field type for a packet");
            }
            foreach (var propertyInfo in packet.GetType().GetProperties())
            {
                if (propertyInfo.PropertyType == typeof(short))
                {
                    PacketEncoder.EncodeShort((short)propertyInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(ushort))
                {
                    PacketEncoder.EncodeUShort((ushort)propertyInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(int))
                {
                    PacketEncoder.EncodeInt((int)propertyInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(char))
                {
                    PacketEncoder.EncodeChar((char)propertyInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(string))
                {
                    PacketEncoder.EncodeString((string)propertyInfo.GetValue(packet), ref packetData);
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(byte[]))
                {
                    PacketEncoder.EncodeByteArray((byte[])propertyInfo.GetValue(packet), ref packetData);
                    continue;
                }
                throw new NotImplementedException(propertyInfo.PropertyType.ToString() + " is not a supported Property type for a packet");
            }
            return packetData;
        }

        public static Packet decodePacket(byte[] packetData, OakNetEndPoint endPoint){
            var packetID = PacketDecoder.DecodeUShort(ref packetData);
            var packetType = getPacketType(packetID);
            //if(packetType != typeof (PingPacket) && packetType != typeof(PongPacket)) 
                //Logger.log("<-- " + packetType);
            var packet = (Packet) Activator.CreateInstance(packetType);
            foreach (var fieldInfo in packetType.GetFields())
            {
                if (fieldInfo.FieldType == typeof(short))
                {
                    fieldInfo.SetValue(packet, PacketDecoder.DecodeShort(ref packetData));
                    continue;
                }
                if (fieldInfo.FieldType == typeof(ushort))
                {
                    fieldInfo.SetValue(packet, PacketDecoder.DecodeUShort(ref packetData));
                    continue;
                }
                if (fieldInfo.FieldType == typeof(int))
                {
                    fieldInfo.SetValue(packet, PacketDecoder.DecodeInt(ref packetData));
                    continue;
                }
                if (fieldInfo.FieldType == typeof(char))
                {
                    fieldInfo.SetValue(packet, PacketDecoder.DecodeChar(ref packetData));
                    continue;
                }
                if (fieldInfo.FieldType == typeof(string))
                {
                    fieldInfo.SetValue(packet, PacketDecoder.DecodeString(ref packetData));
                    continue;
                }
                if (fieldInfo.FieldType == typeof(byte[]))
                {
                    fieldInfo.SetValue(packet, PacketDecoder.DecodeByteArray(ref packetData));
                    continue;
                }
            }
            foreach (var propertyInfo in packetType.GetProperties())
            {
                if (propertyInfo.PropertyType == typeof(short))
                {
                    propertyInfo.SetValue(packet, PacketDecoder.DecodeShort(ref packetData));
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(ushort))
                {
                    propertyInfo.SetValue(packet, PacketDecoder.DecodeUShort(ref packetData));
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(int))
                {
                    propertyInfo.SetValue(packet, PacketDecoder.DecodeInt(ref packetData));
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(char))
                {
                    propertyInfo.SetValue(packet, PacketDecoder.DecodeChar(ref packetData));
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(string))
                {
                    propertyInfo.SetValue(packet, PacketDecoder.DecodeString(ref packetData));
                    continue;
                }
                if (propertyInfo.PropertyType == typeof(byte[]))
                {
                    propertyInfo.SetValue(packet, PacketDecoder.DecodeByteArray(ref packetData));
                    continue;
                }
            }
            return ((PacketProcessor) Activator.CreateInstance(_processors[packetID])).processPacket(packet, endPoint);
        }
        /// <summary>
        /// This method needs to be overriden by each individual PacketProcessor
        /// If you want to send a packet back to the sender, your processor can return this packet here
        /// If you don't want to send a packet back, just return null
        /// </summary>
        /// <param name="packet"></param>
        /// <returns>The packet which should be sent back to the sender, null if nothing should be sent</returns>
        public abstract Packet processPacket(Packet packet, OakNetEndPoint endpoint);
    }
}
