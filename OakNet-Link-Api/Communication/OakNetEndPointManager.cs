using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;

namespace OakNetLink.Api.Communication
{
    public class OakNetEndPointManager
    {
        // All connected endpoints
        static Dictionary<Guid, OakNetEndPoint> endPoints = new Dictionary<Guid,OakNetEndPoint>();

        // All available TurnServer
        static List<OakNetEndPoint> turnServerEndpoints = new List<OakNetEndPoint>();

        // Masterserver endpoint
        public static OakNetEndPoint MasterServerEndpoint { get; internal set; }

        // ManagerServer endpoint
        public static OakNetEndPoint ManagerServerEndpoint { get; internal set; }

        public static Guid ownID = Guid.NewGuid();

        public static OakNetEndPoint GetEndPoint(Guid uid)
        {
            return endPoints.GetValueOrDefault(uid);
        }

        public static OakNetEndPoint Notify(IPAddress iPAddress, int port, Guid peerID)
        {
            OakNetEndPoint ep;
            var result = endPoints.TryGetValue(peerID, out ep);
            if (result)
            {
                // If we are connecting right now, the known IP address port combination may differ from the actual peers info 
                // due to different Nat-Types, so we update in that case
                if(ep.ConnectionState == ConnectionState.Connecting)
                {
                    ep.IpAddress = iPAddress;
                    ep.Port = port;
                }
                return ep;
            }

            if (MasterServerEndpoint?.IpAddress.Equals(iPAddress)==true && MasterServerEndpoint?.Port == port)
            {
                MasterServerEndpoint.PeerID = peerID;
                return MasterServerEndpoint;
            }

            if (ManagerServerEndpoint?.IpAddress.Equals(iPAddress) == true && ManagerServerEndpoint?.Port == port)
                return MasterServerEndpoint;

            var newEndpoint = new OakNetEndPoint(iPAddress, port, peerID);
            lock (endPoints)
            {
                //Logger.log("New endpoint: " + iPAddress.ToString() + ":" + port);
                endPoints.Add(peerID, newEndpoint);
                
            }
            return newEndpoint;
        }

        public static IEnumerable<OakNetEndPoint> ConnectedEndpoints()
        {
            lock (endPoints)
            {
                foreach (var ep in endPoints.Values.ToList())
                {
                    yield return ep;
                }
            }
        }

        public static void remove(OakNetEndPoint endpoint)
        {
            //if (endpoint == MasterServerEndpoint)
            //    MasterServerEndpoint = null;
            //if(endpoint == ManagerServerEndpoint)
            //    ManagerServerEndpoint = null;

            lock (endPoints) {
                if (endPoints.ContainsValue(endpoint))
                    endPoints.Remove(endPoints.Where((kp) => kp.Value == endpoint).FirstOrDefault().Key);
            }
        }
    }
}
