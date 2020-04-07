package de.oaknetwork.oaknetlink.api.network.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.mcinterface.DummyClient;
import de.oaknetwork.oaknetlink.api.network.PacketException;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPDisconnectionPacket;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPPacket;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPPingPacket;
import de.oaknetwork.oaknetlink.api.network.utils.PacketData;
import de.oaknetwork.oaknetlink.api.network.utils.PacketInDecoder;
import de.oaknetwork.oaknetlink.api.network.utils.PacketOutEncoder;
import de.oaknetwork.oaknetlink.api.utils.Constants;

/**
 * This represents an endpoint in the UDP Communication
 * 
 * @author Fabian Fila
 */
public class UDPEndpoint {

	public boolean debug = false;
	public String userName = "missingNo";
	public String uuid = "";

	UDPEndpoint instance;

	private InetAddress udpAddress;
	private int udpPort;
	private boolean connected = true;
	private DummyClient dummyClient;

	// incoming
	private long timeSinceLastPacketIn = System.currentTimeMillis();
	private short currentIncomingPacketNumber = -1;
	private short totalSubPacketNumber = 1;
	private Map<Short, byte[]> incomingPacketQueue = new HashMap<Short, byte[]>();
	private boolean canPacketBeProcessed = false;

	// outgoing
	private byte outgoingPacketNumber = 1;
	// Status -1 = nothing, 0 = waiting; 1 = OK; 2 = ERROR
	private byte status = -1;
	private List<PacketData> outgoingPacketQueue = new ArrayList<PacketData>();

	public UDPEndpoint(InetAddress iaddress, int port) {
		this.udpAddress = iaddress;
		this.udpPort = port;
		instance = this;
		Thread clientThread = new Thread(new Runnable() {

			@Override
			public void run() {
				boolean resend = true;
				boolean ping = true;
				// Try Catch surrounding anything to catch errors which kill the network thread
				try {
					while (connected) {
						// If we recieved a fullPacket we can process it now
						if (canPacketBeProcessed) {
							canPacketBeProcessed = false;
							sendStatus((byte) 1);
							processFullPacket();
						}
						// We received an error
						if (status == 2 && outgoingPacketQueue.size() > 0) {
							sendPacket(outgoingPacketQueue.get(0));
						}
						// We received an ok
						if (status == 1 && outgoingPacketQueue.size() > 0) {
							outgoingPacketNumber++;
							if (outgoingPacketNumber > 100)
								outgoingPacketNumber = 1;
							outgoingPacketQueue.remove(0);
							status = -1;
						}
						// Nothing to do, so sent the next Packet out
						if (status == -1 && outgoingPacketQueue.size() > 0) {
							sendPacket(outgoingPacketQueue.get(0));
						}
						// Ping
						if (System.currentTimeMillis() % 1000 < 10 && debug) {
							if (ping) {
								ping = false;
								UDPPingPacket.sendPacket(instance);
							}
						} else
							ping = true;

						// Every 2 seconds
						if ((System.currentTimeMillis() - timeSinceLastPacketIn) % 2000 < 10
								&& System.currentTimeMillis() - timeSinceLastPacketIn > 2000) {
							// If we sent a packet and wait for an answer, it probably didn't arrive, so we
							// resent it.
							if (resend && status == 0) {
								sendPacket(outgoingPacketQueue.get(0));
							}
							// If we are waiting for more subPackets but we don't receive more, send an
							// error out.
							if (resend && currentIncomingPacketNumber != -1) {
								sendStatus((byte) 2);
								incomingPacketQueue.clear();
							}
							resend = false;
						} else
							resend = true;

						// Every 15 seconds
						if ((System.currentTimeMillis() - timeSinceLastPacketIn) % 15000 < 10
								&& System.currentTimeMillis() - timeSinceLastPacketIn > 15000) {
							closeConnection("timed out");
						}
						// Sleep to not waste all the CPU time
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					Logger.logException("Error in Network Thread", e, Client.class);
					closeConnection("Fatal error occured.");
				}
			}
		});
		clientThread.start();
	}

	/**
	 * This method is used to send packets to the endpoint. Usually this is done by
	 * UDPPacket.java
	 * 
	 * @param packetToSend the packet to send
	 */
	public void sendPacket(PacketData packetDataOrig) {
		status = 0;
		PacketData packetData = new PacketData();
		packetData.data = packetDataOrig.data.clone();
		short totalSubPackages = packetData.data.length % 506 != 0 ? (byte) (packetData.data.length / 506 + 1)
				: (byte) (packetData.data.length / 506);
		short currentSubPackage = 1;
		while (packetData.data.length > 0) {
			byte[] buffer = new byte[512];
			// Add the status, 0 because we are transmiting data
			buffer[0] = 0;
			// Add the PacketNumber
			buffer[1] = outgoingPacketNumber;
			// Add the SubPackage stuff
			PacketData subPackageData = new PacketData();
			PacketOutEncoder.encodeShort(subPackageData, currentSubPackage);
			PacketOutEncoder.encodeShort(subPackageData, totalSubPackages);
			System.arraycopy(subPackageData.data, 0, buffer, 2, 4);
			// Add the PackageData
			System.arraycopy(packetData.data, 0, buffer, 6,
					packetData.data.length > 506 ? 506 : packetData.data.length);
			packetData.removeBytes(packetData.data.length > 506 ? 506 : packetData.data.length);
			// Prepare and send the Packet
			DatagramPacket packetToSend = new DatagramPacket(buffer, 512, udpAddress, udpPort);
			UDPCommunicator.instance().sendPacketBack(instance, packetToSend);
			currentSubPackage++;
		}
	}

	/**
	 * Calling this closes the connection to this Endpoint and removes them from the
	 * connection list
	 * 
	 * @param reason why the connection was closed
	 */
	public void closeConnection(String reason) {
		Logger.logInfo(userName + " lost connection: " + reason, UDPEndpoint.class);
		if(dummyClient != null)
			dummyClient.closeConnection("Endpoint closed connection.");
		UDPEndpointHelper.removeEndpoint(instance);
		connected = false;
	}

	/**
	 * Calling this sends a DisconnectionPacket and closes the connection
	 * 
	 * @param reason why the connection was closed
	 */
	public void disconnect(String reason) {
		UDPDisconnectionPacket.sendPacket(instance, reason);
		instance.closeConnection(reason);
	}

	/**
	 * This sends a short status message back to the endpoint.
	 * 
	 * 1 = OK: The packet arrived and the next packet could be sent
	 * 
	 * 2 = ERROR: There was something wrong with the packet and it should be resent
	 * 
	 * @param status
	 */
	public void sendStatus(byte status) {
		byte[] buffer = new byte[1];
		buffer[0] = status;
		DatagramPacket errorPacket = new DatagramPacket(buffer, 1, udpAddress, udpPort);
		UDPCommunicator.instance().sendPacketBack(this, errorPacket);
	}

	/**
	 * This is called when the full Packet arrived and we can start processing it.
	 */
	public void processFullPacket() {
		PacketData packetData = new PacketData();
		for (short i = 1; i <= totalSubPacketNumber; i++) {
			packetData.appendBytes(incomingPacketQueue.get(i));
		}
		totalSubPacketNumber = -1;
		incomingPacketQueue.clear();
		try {
			UDPPacket.decodePacket(packetData, this);
		} catch (PacketException e) {
			Logger.logException("An error occured", e, UDPEndpoint.class);
		}
	}

	/**
	 * This is called when a new DatagramPacket has been received by the UDP
	 * Communicator
	 * 
	 * @param dpacket the packet which arrived
	 * @throws PacketException
	 */
	public void processDPacket(DatagramPacket dpacket) throws PacketException {
		timeSinceLastPacketIn = System.currentTimeMillis();
		PacketData packetData = new PacketData();
		packetData.appendBytes(dpacket.getData());

		// Decode status
		if (packetData.data[0] != 0) {
			if (packetData.data[0] == 1 || packetData.data[0] == 2) {
				status = packetData.data[0];
			} else {
				throw new PacketException("Recieved illegal status");
			}
			return;
		}
		packetData.removeBytes(1);

		// decode the packetNumber
		if (currentIncomingPacketNumber == -1)
			currentIncomingPacketNumber = packetData.data[0];
		if (currentIncomingPacketNumber != packetData.data[0])
			throw new PacketException("Recieved unexpected packet");
		packetData.removeBytes(1);

		short currentSubPacket = PacketInDecoder.decodeShort(packetData);
		short totalSubPackets = PacketInDecoder.decodeShort(packetData);

		if (Constants.NETWORKDEBUG)
			Logger.logInfo("SubPackage " + currentSubPacket + "/" + totalSubPackets, UDPEndpoint.class);

		totalSubPacketNumber = totalSubPackets;
		incomingPacketQueue.put(currentSubPacket, packetData.data);
		if (incomingPacketQueue.size() == totalSubPackets) {
			canPacketBeProcessed = true;
			currentIncomingPacketNumber = -1;
		}

	}

	public InetAddress udpAddress() {
		return udpAddress;
	}

	public int udpPort() {
		return udpPort;
	}
	
	/**
	 * gets the dummyClient which is associated with this endpoint
	 * 
	 * @return
	 */
	public DummyClient dummyClient() {
		if(dummyClient == null || !dummyClient.connected)
			dummyClient = new DummyClient(instance);
		return dummyClient;
	}

	/**
	 * This method adds a set of bytes to the outgoing queue
	 * 
	 * @param bytes
	 */
	public void addToOutgoingQueue(PacketData packetData) {
		outgoingPacketQueue.add(packetData);
	}
}
