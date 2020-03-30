package de.oaknetwork.oaknetlink.masterserver.network.tcp;

import java.util.ArrayList;

public class TCPClientHelper {

	private static ArrayList<TCPClient> connectedClients = new ArrayList<TCPClient>();

	public static void addClient(TCPClient client) {
		connectedClients.add(client);
		System.out.println(client.tcpSocket.getInetAddress().toString() + " connected via TCP.");
	}
}
