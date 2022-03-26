using OakNetLink.Api;
using OakNetLink.Api.Communication;
using OakNetLink.Tunnel.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel
{
    internal class TunnelConnectionHelper
    {
        private static Dictionary<int, OakNetEndPoint> addressEndpointMap = new Dictionary<int,OakNetEndPoint>();

        public static void addEndpoint(int address, OakNetEndPoint endPoint)
        {
            lock (addressEndpointMap)
            {
                addressEndpointMap[address] = endPoint;
            }
        }

        public static void removeAddress(int address)
        {
            lock (addressEndpointMap)
            {
                addressEndpointMap.Remove(address);
            }
        }

        public static OakNetEndPoint getEndpointForAddress(int address)
        {
            if (addressEndpointMap.ContainsKey(address))
            {
                OakNetEndPoint endpoint = null;
                lock (addressEndpointMap)
                {
                    endpoint = addressEndpointMap[address];
                }
                if(endpoint == null)
                    return null;
                if(endpoint.ConnectionState == ConnectionState.Connected)
                    return endpoint;
                else
                    lock(addressEndpointMap){
                        addressEndpointMap.Remove(address);
                    }
            }
            var packet = new TunnelConnectionRequestPacket();
            packet.Address = address;
            ONL.MasterServer.SendPacket(packet, true);
            lock (addressEndpointMap)
            {
                addressEndpointMap.Add(address, null);
            }
            Task.Factory.StartNew(() =>
            {
                Thread.Sleep(60000);
                lock (addressEndpointMap)
                {
                    if (!addressEndpointMap.ContainsKey(address))
                        return;
                    if (addressEndpointMap[address] != null)
                        return;
                    addressEndpointMap.Remove(address);
                }

                Logger.log("Endpoint Response took too long!");
            });
            return null;
        }

    }
}
