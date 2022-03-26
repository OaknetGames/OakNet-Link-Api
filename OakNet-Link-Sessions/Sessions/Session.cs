using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

namespace OakNetLink.Sessions
{
    public class Session
    {
        public String? Name { get; internal set; }
        public String? Password { get; internal set; }
        internal List<OakNetEndPoint> oakNetEndPoints = new List<OakNetEndPoint>();
        public IEnumerable<OakNetEndPoint> OakNetEndPoints { get { return oakNetEndPoints.AsEnumerable(); } }
            
    }
}
