package de.oaknetwork.oaknetlink.api.network.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPHandshakePacket;
import de.oaknetwork.oaknetlink.api.utils.Constants;

/**
 * This class manages all the UDP endpoint handling.
 * 
 * @author Fabian Fila
 */
public class UDPEndpointHelper {
	
	public static UDPEndpoint masterServerEndpoint;

	private static List<UDPEndpoint> connectedClients = new ArrayList<UDPEndpoint>();

	public static void addEndpoint(UDPEndpoint endpointToAdd) {
		for(UDPEndpoint endpoint : connectedClients) {
			if(endpoint.udpAddress().equals(endpointToAdd.udpAddress())&&endpoint.udpPort() == endpointToAdd.udpPort())
				return;
		}
		connectedClients.add(endpointToAdd);
		Logger.logInfo(endpointToAdd.udpAddress().toString() + ":" + endpointToAdd.udpPort() + " connected via UDP.", UDPEndpointHelper.class);
	}

	public static void removeEndpoint(UDPEndpoint endpointToRemove) {
		if (connectedClients.contains(endpointToRemove)) {
			connectedClients.remove(endpointToRemove);
			Logger.logInfo(endpointToRemove.udpAddress().toString() + " was removed from the endpoint list.",
					UDPEndpointHelper.class);
		} else {
			Logger.logInfo(
					endpointToRemove.udpAddress().toString() + " cannot be removed, because they are not in the list.",
					UDPEndpointHelper.class);
		}
	}

	public static UDPEndpoint endpointByPacket(DatagramPacket dpacket) {
		UDPEndpoint result = null;
		for (UDPEndpoint client : connectedClients) {
			if (client.udpAddress().equals(dpacket.getAddress()) && client.udpPort() == dpacket.getPort())
				result = client;
		}
		if (result == null) {
			result = new UDPEndpoint(dpacket.getAddress(), dpacket.getPort());
			addEndpoint(result);
		}
		return result;
	}
	
	public static UDPEndpoint endpointByClient(Client client) {
		UDPEndpoint result = null;
		for(UDPEndpoint endpoint : connectedClients) {
			if(client.name.equals(endpoint.userName)&&client.uuid.equals(endpoint.uuid))
				result=endpoint;
		}
		return result;
	}
	
	public static UDPEndpoint endpointByAdressPort(String address, int port) {
		UDPEndpoint result = null;
		for(UDPEndpoint endpoint : connectedClients) {
			if(endpoint.udpAddress().getHostAddress().equals(address)&&endpoint.udpPort()==port)
				result=endpoint;
		}
		return result;
	}
	
	
	/**
	 * Calling this method will create a MasterServer endpoint and connects to it;
	 * 
	 * @return the created endpoint
	 */
	public static UDPEndpoint createMasterServerEndpoint() {
		try {
			masterServerEndpoint = new UDPEndpoint(InetAddress.getByName(Constants.MASTERSERVERADDRESS), Constants.UDPPORT);
			masterServerEndpoint.userName = "MasterServer";
			addEndpoint(masterServerEndpoint);
			UDPHandshakePacket.sendPacket(masterServerEndpoint);
			return masterServerEndpoint;
		} catch (UnknownHostException e) {
			Logger.logException("Can't create MasterServer Endpoint", e, UDPEndpointHelper.class);
		}
		return null;
	}

}
