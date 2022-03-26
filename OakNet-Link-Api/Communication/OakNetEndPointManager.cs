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
        static Dictionary<string, OakNetEndPoint> endPoints = new Dictionary<string,OakNetEndPoint>();

        // All available TurnServer
        static List<OakNetEndPoint> turnServerEndpoints = new List<OakNetEndPoint>();

        // Masterserver endpoint
        public static OakNetEndPoint MasterServerEndpoint { get; internal set; }

        // ManagerServer endpoint
        public static OakNetEndPoint ManagerServerEndpoint { get; internal set; }

        public static OakNetEndPoint GetEndPoint(string address, int port)
        {
            return endPoints.GetValueOrDefault(address+":"+port);
        }

        public static OakNetEndPoint Notify(IPAddress iPAddress, int port)
        {
            OakNetEndPoint ep;
            var result = endPoints.TryGetValue(iPAddress.ToString()+":"+port, out ep);
            if (result)
                return ep;

            if (MasterServerEndpoint?.IpAddress.Equals(iPAddress)==true && MasterServerEndpoint?.Port == port)
                return MasterServerEndpoint;

            if (ManagerServerEndpoint?.IpAddress.Equals(iPAddress) == true && ManagerServerEndpoint?.Port == port)
                return MasterServerEndpoint;

            var newEndpoint = new OakNetEndPoint(iPAddress, port);
            lock (endPoints)
            {
                //Logger.log("New endpoint: " + iPAddress.ToString() + ":" + port);
                endPoints.Add(iPAddress.ToString() + ":" + port, newEndpoint);
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

        //TODO is this relevant? This layer should have nothing to do with sessions...
        public static void Update()
        {
            if(endPoints.Count==0)
            {
                // TODO Are we alone in the Session?
            }
        }

    }
}
