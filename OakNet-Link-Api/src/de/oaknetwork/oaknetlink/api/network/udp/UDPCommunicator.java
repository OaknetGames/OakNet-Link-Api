package de.oaknetwork.oaknetlink.api.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.PacketException;
import de.oaknetwork.oaknetlink.api.utils.Constants;

/**
 * This is the heart of the UDP Connection
 * 
 * Because it's a peer to peer communication this can be Sender and Receiver.
 * 
 * @see UDPProtocol.txt for more info
 * 
 * @author Fabian Fila
 */
public class UDPCommunicator {

	private static UDPCommunicator instance;
	private DatagramSocket serverSocket;

	/**
	 * This constructor is used at mod side
	 * 
	 * At mod side the Communicator always starts as client to allow
	 * UDP-Hole-Punching
	 */
	public UDPCommunicator() {
		instance = this;
		Logger.logInfo("Create new UDPCommunicator", UDPCommunicator.class);
		try {
			serverSocket = new DatagramSocket(5289);
			startNetworkThread();
		} catch (SocketException e) {
			Logger.logException("Can't create UDPDatagrammSocket", e, UDPCommunicator.class);
		}
	}

	/**
	 * This constructor is used at Master-Server side
	 * 
	 * Providing a port sets the Communicator automatic in listening mode on a
	 * distinct endpoint.
	 * 
	 * The clients will use the UDP site on the Master Server to exchange their
	 * addresses.
	 */
	public UDPCommunicator(int port) {
		instance = this;
		Logger.logInfo("Create new UDPCommunicator on port: " + port, UDPCommunicator.class);
		try {
			serverSocket = new DatagramSocket(port);
			startNetworkThread();
		} catch (SocketException e) {
			Logger.logException("Can't create UDPDatagrammSocket", e, UDPCommunicator.class);
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
						Logger.logException("Error while recieving packet", eio, UDPCommunicator.class);
					}
					
					if(Constants.NETWORKDEBUG) {
						Logger.logInfo("Got new Packet from: " + packet.getAddress() + ":" + packet.getPort(), UDPCommunicator.class);
						Logger.logInfo("DATA:" + Arrays.toString(packet.getData()), UDPCommunicator.class);
					}
					
					try {
						
						UDPEndpointHelper.endpointByPacket(packet).processDPacket(packet);
					} catch (PacketException e) {
						Logger.logException("Can't process packet", e, UDPCommunicator.class);
					}
				}
			}
		});
		udpNetworkThread.start();
	}

	/**
	 * This sends an UDP DatagramPacket to the specified UDP Client
	 */
	public void sendPacketBack(UDPEndpoint client, DatagramPacket packetToSendBack) {
		try {
			serverSocket.send(packetToSendBack);
		} catch (IOException e) {
			Logger.logException("Can't send packet", e, UDPCommunicator.class);
		}
	}

	/**
	 * @return the current instance of the UDPCommunicator
	 */
	public static UDPCommunicator instance() {
		return instance;
	}

}
