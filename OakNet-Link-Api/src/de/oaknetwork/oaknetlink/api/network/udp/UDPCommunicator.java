package de.oaknetwork.oaknetlink.api.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.PacketException;

public class UDPCommunicator {

	private static UDPCommunicator instance;
	private DatagramSocket serverSocket;

	// Mod side
	public UDPCommunicator() {
		instance = this;
		Logger.logInfo("Create new UDPCommunicator", UDPCommunicator.class);
		try {
			serverSocket = new DatagramSocket();
		} catch (SocketException e) {
			Logger.logException("Can't create UDPDatagrammSocket" , e, UDPCommunicator.class);
		}
	}
	
	// Server side 
	public UDPCommunicator(int port) {
		instance = this;
		Logger.logInfo("Create new UDPCommunicator on port: " + port, UDPCommunicator.class);
		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			Logger.logException("Can't create UDPDatagrammSocket" , e, UDPCommunicator.class);
		}
	}

	private void startNetworkThread() {
		Thread udpNetworkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					byte[] buf = new byte[512];
					DatagramPacket packet = new DatagramPacket(buf, 512);
					try {
						serverSocket.receive(packet);
					} catch (IOException eio) {
						Logger.logException("Error while recieving packet" , eio, UDPCommunicator.class);
					}
					try {
						UDPClientHelper.getClientByPacket(packet).processDPacket(packet);
					} catch (PacketException e) {
						Logger.logException("Can't process packet" , e, UDPCommunicator.class);
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
			Logger.logException("Can't send packet" , e, UDPCommunicator.class);
		}
	}
	
	public static UDPCommunicator instance() {
		return instance;
	}

}
