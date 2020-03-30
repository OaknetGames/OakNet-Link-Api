package de.oaknetwork.oaknetlink.masterserver.network.tcp;

import java.net.Socket;

public class TCPClient {

	public Socket tcpSocket;
	public String name = "";
	public String uuid = "";

	public TCPClient(Socket tcpSocket) {
		this.tcpSocket = tcpSocket;
	}

}
