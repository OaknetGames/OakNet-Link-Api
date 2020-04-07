package de.oaknetwork.oaknetlink.api.mcinterface;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPMinecraftDataPacket;
import de.oaknetwork.oaknetlink.api.network.utils.BytePackage;

/**
 * This Client connects to a Minecraft Server and redirects packets from our UDP
 * tunnel
 * 
 * @author Fabian Fila
 */
public class DummyClient {
	
	/**
	 * This is true, when the client is connected to a server
	 */
	public boolean connected = false;

	private Socket server;
	private Thread networkThread;

	/**
	 * Tries to establish a connection to the MasterServer
	 */
	public DummyClient(UDPEndpoint host) {
		if (networkThread != null && networkThread.isAlive())
			return;
		networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Try Catch surrounding anything to catch errors which kill the network thread
				try {
					// Connect to Server
					try {
						server = new Socket(InetAddress.getByName("127.0.0.1"), 25565);
					} catch (IOException e) {
						Logger.logException("Can't connect to MasterServer", e, DummyClient.class);
						return;
					}
					connected = true;

					// Create the InputStream
					BufferedInputStream in = null;
					try {
						in = new BufferedInputStream(server.getInputStream());
					} catch (IOException e1) {
						Logger.logException("Can't get Inputstream", e1, DummyClient.class);
						return;
					}
					while (connected) {
						try {
							// Decode Packet Length
							int packetLength = MinecraftPacketInDecoder.decodeVarInt(in);
							Logger.logInfo("Blub", DummyClient.class);
							byte[] data = new byte[packetLength];
							if (in.read(data, 0, packetLength) != packetLength)
								throw new IOException("Received to few bytes, expected " + packetLength);

							UDPMinecraftDataPacket.sendPacket(host, new BytePackage(data));
						} catch (SocketException e) {
							if (e.getMessage().contains("Connection reset")) {
								closeConnection("Reset by peer");
								return;
							} else
								Logger.logException("Error while communicating with Master Server", e, DummyClient.class);
							break;
						} catch (IOException e) {
							Logger.logException("Error while communicating with Master Server", e, DummyClient.class);
							break;
						}
					}
					closeConnection("An error occured");
				} catch (Exception e) {
					Logger.logException("Error in Network Thread", e, DummyClient.class);
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
	public void closeConnection(String reason) {
		Logger.logInfo("Disconnected from MinecraftServer: " + reason, DummyClient.class);
		connected = false;
	}

	/**
	 * Sends, when possible a packet to the Client
	 * 
	 * @param packetData the Packet to send
	 */
	public void sendPacket(byte[] data) {
		if (!connected) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!connected) {
			Logger.logError("Can't send packets to MasterServer while not connected", Client.class);
			return;
		}
		try {
			// Send packet out
			server.getOutputStream().write(data);
			server.getOutputStream().flush();
		} catch (IOException e) {
			Logger.logException("Can't send packet to MasterServer", e, Client.class);
			if (e instanceof SocketException)
				connected = false;
		}
	}
}
