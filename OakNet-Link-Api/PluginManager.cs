using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Api
{
    internal class PluginManager
    {
        static Dictionary<byte, ONLPlugin> plugins = new Dictionary<byte, ONLPlugin>();
        internal static void addPlugin(ONLPlugin plugin)
        {
            if (plugins.Count == 0)
            {
                var internalPlugin = new InternalPlugin();
                plugins.Add(internalPlugin.pluginID, internalPlugin);
                Logger.log("Registered internal plugin");
                ushort id_ = (ushort) (internalPlugin.pluginID << 8);
                foreach (var packet in internalPlugin.registerPackets())
                {
                    PacketProcessor.addPacket(packet, id_++);
                }
            }
            if (plugins.ContainsKey(plugin.pluginID))
                throw new Exception($"Plugin with ID: {plugin.pluginID} already registered!");
            plugins.Add(plugin.pluginID, plugin);
            ushort id = (ushort) (plugin.pluginID << 8);
            Logger.log("Registered plugin with ID: " + plugin.pluginID);
            foreach (var packet in plugin.registerPackets())
            {
                PacketProcessor.addPacket(packet, id++);
            }
        }
    }
}
