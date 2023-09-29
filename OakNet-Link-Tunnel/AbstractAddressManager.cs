using OakNetLink.Api.Communication;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.Tunnel
{
    public abstract class AbstractAddressManager
    {
        public abstract int getAddress(String canonicalName, OakNetEndPoint endPoint);

        public abstract int getAddress(OakNetEndPoint endPoint);

        public abstract OakNetEndPoint getEndPoint(int address);
    }
}
