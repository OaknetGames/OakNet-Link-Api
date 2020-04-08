package de.oaknetwork.oaknetlink.api.mcinterface;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
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
	 * Creates a new DummyClient
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
						Logger.logException("Can't connect to MinecraftServer", e, DummyClient.class);
						return;
					}
					connected = true;

					// Create the InputStream
					DataInputStream in = null;
					try {
						in = new DataInputStream(server.getInputStream());
					} catch (IOException e1) {
						Logger.logException("Can't get Inputstream", e1, DummyClient.class);
						return;
					}
					while (connected) {
						try {

							Logger.logInfo("Blib", DummyServer.class);
							// Decode Packet Length
							Tuple<Integer, PacketData> decodedData = MinecraftPacketInDecoder.decodeVarInt(in);
							int packetLength = decodedData.value1;
							Logger.logInfo("Blub", DummyClient.class);
							byte[] data = new byte[packetLength];
							
							in.readFully(data, 0, packetLength);
		
							decodedData.value2.appendBytes(data);
							UDPMinecraftDataPacket.sendPacket(host, new BytePackage(decodedData.value2.data));
						} catch (SocketException e) {
							if (e.getMessage().contains("Connection reset")) {
								closeConnection("Reset by peer");
								return;
							} else
								Logger.logException("Error while communicating with MinecraftServer", e, DummyClient.class);
							break;
						} catch (IOException e) {
							Logger.logException("Error while communicating with MinecraftServer", e, DummyClient.class);
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
