package de.oaknetwork.oaknetlink.masterserver.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.network.PacketException;
import de.oaknetwork.oaknetlink.masterserver.network.tcp.TCPClient;

public class UDPCommunicator {

	static UDPCommunicator instance;
	private DatagramSocket serverSocket;
	
	public UDPCommunicator(int port) {
		instance=this;
		System.out.println("Create new UDPCommunicator on port: " + port);
		try {
			serverSocket=new DatagramSocket(port);
		} catch (SocketException e) {
			System.err.println("Can't create UDPDatagrammSocket: " + e.getMessage());
			e.printStackTrace(System.err);
		}
		Thread udpNetworkThread= new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					byte[] buf = new byte[512];
					DatagramPacket packet=new DatagramPacket(buf, 512);
					try {
						UDPClientHelper.getClientByPacket(packet).processDPacket(packet);
					} catch (PacketException e) {
						System.err.println("Can't process Packet: " + e.getMessage());
						e.printStackTrace(System.err);
					}
				}
			}
		});
		udpNetworkThread.start();
	}
	
	public void sendPacketBack(UDPClient client, DatagramPacket packetToSendBack) {
		try {
			serverSocket.send(packetToSendBack);
		} catch (IOException e) {
			System.err.println("Can't send Packet: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
}
