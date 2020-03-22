package de.oaknetwork.oaknetlink.masterserver.network.tcp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class TCPClient {
	
	public Socket tcpSocket;
	public String name="";
	public String uuid="";
	
	public TCPClient(Socket tcpSocket) {
		this.tcpSocket=tcpSocket;
	}
	

}
