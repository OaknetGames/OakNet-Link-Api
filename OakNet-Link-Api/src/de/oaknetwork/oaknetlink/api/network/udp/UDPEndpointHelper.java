package de.oaknetwork.oaknetlink.api.network.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPHandshakePacket;
import de.oaknetwork.oaknetlink.api.utils.Constants;

/**
 * This class manages all the UDP endpoint handling.
 * 
 * @author Fabian Fila
 */
public class UDPEndpointHelper {

	private static List<UDPEndpoint> connectedClients = new ArrayList<UDPEndpoint>();

	public static void addEndpoint(UDPEndpoint endpointToAdd) {
		connectedClients.add(endpointToAdd);
		Logger.logInfo(endpointToAdd.udpAdress().toString() + " connected via UDP.", UDPEndpointHelper.class);
	}

	public static void removeEndpoint(UDPEndpoint endpointToRemove) {
		if (connectedClients.contains(endpointToRemove)) {
			connectedClients.remove(endpointToRemove);
			Logger.logInfo(endpointToRemove.udpAdress().toString() + " was removed from the endpoint list.",
					UDPEndpointHelper.class);
		} else {
			Logger.logInfo(
					endpointToRemove.udpAdress().toString() + " cannot be removed, because they are not in the list.",
					UDPEndpointHelper.class);
		}
	}

	public static UDPEndpoint getClientByPacket(DatagramPacket dpacket) {
		UDPEndpoint result = null;
		for (UDPEndpoint client : connectedClients) {
			if (client.udpAdress().equals(dpacket.getAddress()) && client.udpPort() == dpacket.getPort())
				result = client;
			// result = client;
		}
		if (result == null) {
			result = new UDPEndpoint(dpacket.getAddress(), dpacket.getPort());
			addEndpoint(result);
		}
		return result;
	}
	
	/**
	 * Calling this method will create a MasterServer endpoint and connects to it;
	 * 
	 * @return the created endpoint
	 */
	public static UDPEndpoint createMasterServerEndpoint() {
		UDPEndpoint masterServerEndpoint;
		try {
			masterServerEndpoint = new UDPEndpoint(InetAddress.getByName(Constants.MASTERSERVERADDRESS), Constants.UDPPORT);
			masterServerEndpoint.userName = "MasterServer";
			addEndpoint(masterServerEndpoint);
			UDPHandshakePacket.sendPacket(masterServerEndpoint);
			return masterServerEndpoint;
		} catch (UnknownHostException e) {
			Logger.logException("Can't create MasterServerEndpoint", e, UDPEndpointHelper.class);
		}
		return null;
	}

}
