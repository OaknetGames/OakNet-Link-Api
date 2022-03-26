using OakNetLink.Api.Communication;
using System;
using System.Linq;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets.Internal
{
    internal class LargePacketPacketProcessor : PacketProcessor
    {
        public override Packet processPacket(Packet packet, OakNetEndPoint endpoint)
        {
            var largePacketPacket = packet as LargePacketPacket;

            lock(queue)
            {
                if (!queue.ContainsKey(endpoint))
                    queue.Add(endpoint, new List<LargePacketPacket>());
                queue[endpoint].Add(largePacketPacket);
            }
            if (largePacketPacket.lastPacket > 0)
            {
                byte[] reAssembledPacketData;
                lock (queue)
                {
                    reAssembledPacketData = queue[endpoint].SelectMany((p) => p.data).ToArray();
                }
                if (Communicator.instance.isServer && ((largePacketPacket.lastPacket) >> 1) == 1)
                {
                    if (Communicator.instance.allowBroadcasts)
                    {
                        foreach (var ep in OakNetEndPointManager.ConnectedEndpoints())
                        {
                            Communicator.instance.sendPacket(reAssembledPacketData, endpoint, false, true, false);
                        }
                    }
                }
                else
                    endpoint.handlePacket(reAssembledPacketData, ((largePacketPacket.lastPacket&0x0000000000000010)>>1)==1, true);

                lock (queue)
                {
                    queue[endpoint].Clear();
                }
            }
            return null;
                
        }

        public static Dictionary<OakNetEndPoint, List<LargePacketPacket>> queue = new Dictionary<OakNetEndPoint, List<LargePacketPacket>>();

        public static void makeLargePacket(byte[] packetData, OakNetEndPoint receiver, bool broadcast)
        {
            int i = 0;
            byte[] data;
            LargePacketPacket packetToSend;
            while (packetData.Length - i > 500)
            {
                data = new byte[500];
                Array.Copy(packetData, i, data, 0, data.Length);
                packetToSend = new LargePacketPacket();
                packetToSend.data = data;
                packetToSend.lastPacket = 0;
                Communicator.instance.sendPacket(PacketProcessor.encodePacket(packetToSend), receiver, broadcast, true, false);
                i += 500;
            }
            data = new byte[packetData.Length - i];
            Array.Copy(packetData, i, data, 0, data.Length);
            packetToSend = new LargePacketPacket();
            packetToSend.data = data;
            packetToSend.lastPacket = (short)(((broadcast ? 1 : 0) << 1) + 1);
            Communicator.instance.sendPacket(PacketProcessor.encodePacket(packetToSend), receiver, broadcast, true, false);
        }
    }
}
