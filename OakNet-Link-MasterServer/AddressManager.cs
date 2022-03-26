using OakNetLink.Api.Communication;
using OakNetLink.Tunnel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OakNetLink.MasterServer
{
    internal class AddressManager : AbstractAddressManager
    {
        static ushort newAddresses = 2;

        static List<int> freeAddresses = new List<int>();
        static Dictionary<string, int> addressNameMap = new Dictionary<string, int>();
        static Dictionary<int, OakNetEndPoint> addressMap = new Dictionary<int, OakNetEndPoint>();

        public override int getAddress(OakNetEndPoint endPoint)
        {
            int address = 0;
            if (addressMap.Any(kvp => kvp.Value.Equals(endPoint)))
            {
                address = addressMap.Where(kvp => kvp.Value.Equals(endPoint)).First().Key;
            }
            return address;
        }

        public override int getAddress(string canonicalName, OakNetEndPoint endPoint)
        {
            int address = 0;
            if (addressNameMap.ContainsKey(canonicalName))
            {
                if (addressMap[addressNameMap[canonicalName]].ConnectionState == ConnectionState.Connected)
                    throw new Exception("Canonical name already in use");
                else
                {
                    addressMap.Remove(addressNameMap[canonicalName]);
                    freeAddresses.Add(addressNameMap[canonicalName]);
                    addressNameMap.Remove(canonicalName);
                }
            }
            if(address == 0)
                if(freeAddresses.Count > 0)
                {
                    address = freeAddresses[0];
                    freeAddresses.RemoveAt(0);
                }else
                {
                    if (newAddresses == ushort.MaxValue-1)
                        throw new Exception("No more address space");
                    address = (10<<24) | (6<<16) | (newAddresses);
                    newAddresses++;
                }

            if (addressNameMap.ContainsValue(address))
            {
                addressNameMap.Remove(addressNameMap.Where((kvp) => kvp.Value == address).FirstOrDefault().Key);
            }
            addressMap[address] = endPoint;
            addressNameMap[canonicalName] = address;
            return address;
        }

        public override OakNetEndPoint? getEndPoint(int address)
        {
            return addressMap.ContainsKey(address)? addressMap[address] : null;
        }
    }
}
