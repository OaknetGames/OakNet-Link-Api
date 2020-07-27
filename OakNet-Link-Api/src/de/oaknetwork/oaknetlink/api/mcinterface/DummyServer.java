package de.oaknetwork.oaknetlink.api.mcinterface;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPMinecraftDataPacket;
import de.oaknetwork.oaknetlink.api.network.utils.BytePackage;
import de.oaknetwork.oaknetlink.api.network.utils.PacketData;
import de.oaknetwork.oaknetlink.api.utils.Tuple;

/**
 * This is a Server which accepts connections from Minecraft Clients and routes
 * incoming traffic through our UDP tunnel.
 * 
 * @author Fabian Fila
 */
public class DummyServer {

	private static UDPEndpoint host;
	private static int port = 25316;
	private static int tries = 0;
	private static boolean connected;
	private static ServerSocket serverSocket;
	private static Socket client;

	public static void startServer(UDPEndpoint host) {
		if (tries == 10) {
			tries = 0;
			return;
		}
		DummyServer.host = host;
		serverSocket = null;
		client = null;
		Logger.logInfo("Create new DummyServer on port: " + port, DummyServer.class);
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			tries++;
			port++;
			startServer(host);
			return;
		}

		Thread dummyServerNetworkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					client = serverSocket.accept();
					// Try Catch surrounding anything to catch errors which kill the network thread
					try {
						connected = true;
						// Create the InputStream
						DataInputStream in = null;
						try {
							in = new DataInputStream(client.getInputStream());
						} catch (IOException e1) {
							Logger.logException("Can't get Inputstream", e1, DummyServer.class);
							return;
						}
						// Network loop below
						while (connected) {
							try {
								// Decode Packet Length
								Tuple<Integer, PacketData> decodedData = MinecraftPacketInDecoder.decodeVarInt(in);
								int packetLength = decodedData.value1;
								byte[] data = new byte[packetLength];
								
								in.readFully(data, 0, packetLength);
								
								decodedData.value2.appendBytes(data);
								UDPMinecraftDataPacket.sendPacket(host, new BytePackage(decodedData.value2.data));
							} catch (SocketException e) {
								if (e.getMessage().contains("Connection reset")) {
									closeConnection("Reset by peer");
									return;
								} else
									Logger.logException("Error while communicating with Dummy Server", e,
											DummyServer.class);
								break;
							} catch (IOException e) {
								Logger.logException("Error while communicating with Dummy Server", e,
										DummyServer.class);
								break;
							}
						}
						closeConnection("An error  occured");
					} catch (Exception e) {
						Logger.logException("Error in Network Thread", e, DummyServer.class);
						closeConnection("Fatal error occured.");
					}
				} catch (IOException e) {
					System.err.println("Error while accepting a new connection: " + e.getMessage());
					e.printStackTrace(System.err);
				}

			}
		});
		dummyServerNetworkThread.start();
	}

	/**
	 * Closes the connection
	 * 
	 * @param reason
	 */
	public static void closeConnection(String reason) {
		Logger.logInfo("Disconnected from DummyServer: " + reason, DummyServer.class);
		try {
			client.close();
		} catch (IOException e) {
			Logger.logException("Can't close Socket", e, DummyServer.class);
		}
		client = null;
		connected = false;
		host.disconnect("Minecraft connection closed");
	}

	/**
	 * Sends, when possible a packet to the Client
	 * 
	 * @param packetData the Packet to send
	 */
	public static void sendPacket(byte[] data) {
		if (!connected) {
			Logger.logError("Can't send packets to Minecraft while not connected", Client.class);
			return;
		}
		try {
			// Send packet out
			client.getOutputStream().write(data);
			client.getOutputStream().flush();
		} catch (IOException e) {
			Logger.logException("Can't send packet to MasterServer", e, Client.class);
			if (e instanceof SocketException)
				connected = false;
		}
	}
	
	public static int port() {
		return port;
	}
}
