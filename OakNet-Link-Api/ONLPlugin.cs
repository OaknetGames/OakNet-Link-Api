using OakNetLink.Api.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Api
{
    public abstract class ONLPlugin
    { 
        internal byte pluginID;

        public ONLPlugin(byte pluginID)
        {
            this.pluginID = pluginID;
        }

        /**
        *  summary registers for the plugin needed packets
        *  returns a Dictionary with the key being the type of the packet and 
        *  the value being the type of the corresponding packet processor
        **/
        public abstract List<Type> registerPackets();
    }
}
