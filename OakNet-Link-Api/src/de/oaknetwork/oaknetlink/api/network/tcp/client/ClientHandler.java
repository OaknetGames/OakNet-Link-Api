package de.oaknetwork.oaknetlink.api.network.tcp.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.PacketException;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.Packet;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CDisconnectionPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CHandshackePacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CPingPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;
import de.oaknetwork.oaknetlink.api.network.utils.PacketData;
import de.oaknetwork.oaknetlink.api.network.utils.PacketInDecoder;
import de.oaknetwork.oaknetlink.api.network.utils.PacketOutEncoder;
import de.oaknetwork.oaknetlink.api.utils.Constants;
import de.oaknetwork.oaknetlink.api.utils.HostnameHelper;

/**
 * This is the client side of the TCP communication
 * 
 * @author Fabian Fila
 */
public class ClientHandler {

	/**
	 * This is true, when the client is connected to a server
	 */
	public static boolean connected = false;

	private static Socket server;
	private static Thread networkThread;

	/**
	 * Tries to establish a connection to the MasterServer
	 */
	public static void connect() {
		if (networkThread != null && networkThread.isAlive())
			return;
		networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Try Catch surrounding anything to catch errors which kill the network thread
				try {
					// Connect to Server
					try {
						server = new Socket(HostnameHelper.getIPAddress(), Constants.TCPPORT);
					} catch (IOException e) {
						Logger.logException("Can't connect to MasterServer", e, ClientHandler.class);
						return;
					}
					connected = true;

					// Create the InputStream
					BufferedInputStream in = null;
					try {
						in = new BufferedInputStream(server.getInputStream());
					} catch (IOException e1) {
						Logger.logException("Can't get Inputstream", e1, ClientHandler.class);
						return;
					}
					
					// Send HandshakePacket
					CHandshackePacket.sendPacket();

					// Network loop below
					// Ping Loop
					Thread pingThread = new Thread(new Runnable() {

						@Override
						public void run() {
							boolean ping = false;
							while (connected) {
								// Ping
								if (System.currentTimeMillis() % 1000 < 10) {
									if (ping) {
										ping = false;
										CPingPacket.sendPacket();
									}
								} else
									ping = true;
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					});
					pingThread.start();
					while (connected) {
						try {
							// Decode Packet Length
							PacketData packetData = new PacketData();
							packetData.data = new byte[2];
							if (in.read(packetData.data, 0, 2) != 2)
								throw new IOException("Received to few bytes, expected 2");
							short packetLength = PacketInDecoder.decodeShort(packetData);

							packetData.data = new byte[packetLength];
							if (in.read(packetData.data, 0, packetLength) != packetLength)
								throw new IOException("Received to few bytes, expected " + packetLength);
							Packet.decodePacket(packetData, null);
						} catch (SocketException e) {
							if(e.getMessage().contains("Connection reset")) {
								closeConnection("Reset by peer");
								return;
							}
							else
								Logger.logException("Error while communicating with Master Server", e, Client.class);
							break;
						} catch (IOException e) {
							Logger.logException("Error while communicating with Master Server", e, ClientHandler.class);
							break;
						} catch (PacketException e) {
							Logger.logException("Error while decoding a packet", e, ClientHandler.class);
							break;
						}
					}
					closeConnection("An error occured");
				} catch (Exception e) {
					Logger.logException("Error in Network Thread", e, ClientHandler.class);
					closeConnection("Fatal error occured.");
				}
			}
		});
		networkThread.start();
	}

	/**
	 * Closes the connection
	 * 
	 * @param reason
	 */
	public static void closeConnection(String reason) {
		Logger.logInfo("Disconnected from MasterServer: " + reason, ClientHandler.class);
		connected = false;
	}

	/**
	 * Sends a disconnection packet and closes the connection
	 * 
	 * @param reason
	 */
	public static void disconnect(String reason) {
		CDisconnectionPacket.sendPacket(reason);
		closeConnection(reason);
	}

	/**
	 * Sends, when possible a packet to the MasterServer
	 * 
	 * @param packetData the Packet to send
	 */
	public static void sendPacket(PacketData packetData) {
		if (!connected) {
			Logger.logError("Can't send packets to MasterServer while not connected", ClientHandler.class);
			return;
		}
		try {
			// Encode length 
			PacketData finalPacketData = new PacketData();
			PacketOutEncoder.encodeShort(finalPacketData, (short) packetData.data.length);
			// Add the PacketData
			finalPacketData.appendBytes(packetData.data);
			// Send packet out
			server.getOutputStream().write(finalPacketData.data);
			server.getOutputStream().flush();
		} catch (IOException e) {
			Logger.logException("Can't send packet to MasterServer", e, ClientHandler.class);
			if (e instanceof SocketException)
				connected = false;
		}

	}
}
