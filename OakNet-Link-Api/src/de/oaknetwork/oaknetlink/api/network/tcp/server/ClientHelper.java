package de.oaknetwork.oaknetlink.api.network.tcp.server;

import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.log.Logger;

/**
 * This helper handles the TCP client management stuff
 * 
 * @author Fabian Fila
 */
public class ClientHelper {

	private static ArrayList<Client> connectedClients = new ArrayList<Client>();

	public static void addClient(Client client) {
		connectedClients.add(client);
		Logger.logInfo(client.client.getInetAddress().toString() + " connected via TCP.", ClientHelper.class);
	}
	
	public static void removeClient(Client client) {
		if(!connectedClients.contains(client))
			return;
		connectedClients.remove(client);
		Logger.logInfo(client.client.getInetAddress().toString() + " disconnected from TCP.", ClientHelper.class);
	}
}
