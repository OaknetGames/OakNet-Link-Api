package de.oaknetwork.oaknetlink.api.network.tcp.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.PacketException;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.Packet;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SDisconnectionPacket;
import de.oaknetwork.oaknetlink.api.network.utils.PacketData;
import de.oaknetwork.oaknetlink.api.network.utils.PacketInDecoder;
import de.oaknetwork.oaknetlink.api.network.utils.PacketOutEncoder;

public class Client {

	public Socket client;
	public String name = "";
	public String uuid = "";
	public boolean connected;

	private Client instance;

	public Client(Socket client) {
		this.client = client;
		instance = this;
		startClientThread();
	}

	public void startClientThread() {
		Thread networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Try Catch surrounding anything to catch errors which kill the network thread
				try {
					connected = true;
					// Create the InputStream
					BufferedInputStream in = null;
					try {
						in = new BufferedInputStream(client.getInputStream());
					} catch (IOException e1) {
						Logger.logException("Can't get Inputstream", e1, Client.class);
						return;
					}
					// Network loop below
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


							Packet.decodePacket(packetData, instance);
						} catch (SocketException e) {
							if(e.getMessage().contains("Connection reset")) {
								closeConnection("Reset by peer");
								return;
							}
							else
								Logger.logException("Error while communicating with Master Server", e, Client.class);
							break;
						} catch (IOException e) {
							Logger.logException("Error while communicating with Master Server", e, Client.class);
							break;
						} catch (PacketException e) {
							Logger.logException("Error while decoding a packet", e, Client.class);
							break;
						}
					}
					closeConnection("An error  occured");
				} catch (Exception e) {
					Logger.logException("Error in Network Thread", e, Client.class);
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
		Logger.logInfo("Disconnected from MasterServer: " + reason, Client.class);
		connected = false;
		ClientHelper.removeClient(this);
	}

	/**
	 * Sends a disconnection packet and closes the connection
	 * 
	 * @param reason
	 */
	public void disconnect(String reason) {
		SDisconnectionPacket.sendPacket(reason, instance);
		closeConnection(reason);
	}

	/**
	 * Sends, when possible a packet to the Client
	 * 
	 * @param packetData the Packet to send
	 */
	public void sendPacket(PacketData packetData) {
		if (!connected) {
			Logger.logError("Can't send packets to MasterServer while not connected", Client.class);
			return;
		}
		try {
			// Encode length
			PacketData finalPacketData = new PacketData();
			PacketOutEncoder.encodeShort(finalPacketData, (short) packetData.data.length);
			// Add the PacketData
			finalPacketData.appendBytes(packetData.data);
			// Send packet out
			client.getOutputStream().write(finalPacketData.data);
			client.getOutputStream().flush();
		} catch (IOException e) {
			Logger.logException("Can't send packet to MasterServer", e, Client.class);
			if (e instanceof SocketException)
				connected = false;
		}

	}

}
