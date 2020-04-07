package de.oaknetwork.oaknetlink.api.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class provides some helper methods for resolving Hostnames
 * 
 * @author Fabian Fila
 */
public class HostnameHelper {

	/**
	 * This method returns either the IPv6 or the IPv4 of the Masterserver.
	 * 
	 * By default it prefers IPv6 over IPv4, however it respects the forceIPv4 flag
	 * from the Constants class
	 * 
	 * @return
	 * @throws UnknownHostException 
	 */
	public static InetAddress getIPAddress() throws UnknownHostException {
		InetAddress result = null;
		for (InetAddress addr : InetAddress.getAllByName(Constants.MASTERSERVERADDRESS)) {
			if (addr instanceof Inet6Address) {
				result = addr;
				break;
			}
		}
		if(result == null || Constants.FORCEIPV4) {
			for (InetAddress addr : InetAddress.getAllByName(Constants.MASTERSERVERADDRESS)) {
				if (addr instanceof Inet4Address) {
					result = addr;
					break;
				}
			}
		}
		if(result == null) 
			throw new UnknownHostException();
		return result;
	}
}
