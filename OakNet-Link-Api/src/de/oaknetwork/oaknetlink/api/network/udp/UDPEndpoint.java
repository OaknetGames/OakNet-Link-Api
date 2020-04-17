package de.oaknetwork.oaknetlink.api.network.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
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

	// blocks
	Object inBlock = new Object();
	Object outBlock = new Object();

	// Threads
	Thread loopThread;
	Thread receiveThread;
	Thread sendThread;

	// incoming
	private long timeSinceLastPacketIn = System.currentTimeMillis();
	private byte currentIncomingPacketNumber = 1;
	private byte previousIncomingPacketNumber = 100;
	private byte[] incomingPacket;
	private List<PacketData> incomingPacketQueue = new ArrayList<PacketData>();
	private int incomingSubPacketNumber = 0;

	// outgoing
	private byte outgoingPacketNumber = 1;
	// Status -1 = nothing, 0 = waiting; 1 = OK; 2 = ERROR
	private byte status = -1;
	private byte payLoad = -1;
	private List<PacketData> outgoingPacketQueue = new ArrayList<PacketData>();

	public UDPEndpoint(InetAddress iaddress, int port) {
		this.udpAddress = iaddress;
		this.udpPort = port;
		instance = this;
		loopThread = new Thread(new Runnable() {

			@Override
			public void run() {
				int timer = 0;
				// Try Catch surrounding anything to catch errors which kill the network thread
				try {
					while (connected) {
						// Ping
						if (debug) {
							Logger.logInfo("Status: " + outgoingPacketQueue.size(), UDPEndpoint.class);
							UDPPingPacket.sendPacket(instance);
						}
						// Every 2 seconds
						if (timer % 2 == 0 && System.currentTimeMillis() - timeSinceLastPacketIn > 2000) {
							// If we sent a packet and wait for an answer, it probably didn't arrive, so we
							// resent it.
							if (status == 0) {
								sendPacket(outgoingPacketQueue.get(0));
							}
							// If we are waiting for more subPackets but we don't receive more, send an
							// error out.
							if (incomingPacket != null) {
								sendStatus((byte) 2, currentIncomingPacketNumber);
								incomingPacket = null;
								incomingSubPacketNumber = 0;
							}
						}

						// After 15 seconds without a Packet
						if (System.currentTimeMillis() - timeSinceLastPacketIn > 15000) {
							closeConnection("timed out");
						}
						if (timer == 99)
							timer = 0;
						else
							timer++;

						// Sleep to not waste all the CPU time
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							Logger.logWarning("Network Loop Thread stopped", UDPEndpoint.class);
						}
					}
				} catch (Exception e) {
					Logger.logException("Error in Network Loop Thread", e, Client.class);
					closeConnection("Fatal error occured.");
				}
			}
		});
		loopThread.start();

		receiveThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (connected) {
					// Block until a new Packet arrives
					synchronized (inBlock) {
						try {
							inBlock.wait();
						} catch (InterruptedException e) {
							Logger.logWarning("Network Receive Thread stopped", UDPEndpoint.class);
						}
						try {
							// If we recieved a fullPacket we can process it now
							while (incomingPacketQueue.size() > 0) {
								processFullPacket(incomingPacketQueue.get(0));
								incomingPacketQueue.remove(0);
							}
							// We received an error
							if (status == 2 && outgoingPacketQueue.size() > 0) {
								if (payLoad == outgoingPacketNumber)
									sendPacket(outgoingPacketQueue.get(0));
								else
									Logger.logWarning("Received an error but packet is not current. Ignoring...",
											UDPEndpoint.class);
							}
							// We received an ok
							if (status == 1) {
								if (payLoad == outgoingPacketNumber) {
									if (outgoingPacketQueue.size() > 0) {
										outgoingPacketNumber++;
										if (outgoingPacketNumber > 100)
											outgoingPacketNumber = 1;
										outgoingPacketQueue.remove(0);
										status = -1;
									}
								} else {
									Logger.logWarning("Received an ok but packet is not current. Ignoring...",
											UDPEndpoint.class);
								}
							}
						} catch (Exception e) {
							Logger.logException("Error in Network Receive Thread", e, Client.class);
							closeConnection("Fatal error occured.");
						}
					}
				}

			}
		});
		receiveThread.start();

		sendThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (connected) {
					// Block until a new Packet has to be sent out or we received an OK
					synchronized (outBlock) {
						try {
							if (outgoingPacketQueue.size() == 0)
								outBlock.wait();
						} catch (InterruptedException e) {
							Logger.logWarning("Network Send Thread stopped", UDPEndpoint.class);
						}
						try {
							if (status == -1 && outgoingPacketQueue.size() > 0) {
								sendPacket(outgoingPacketQueue.get(0));
							}

						} catch (Exception e) {
							Logger.logException("Error in Network Send Thread", e, Client.class);
							closeConnection("Fatal error occured.");
						}
					}
				}

			}
		});
		sendThread.start();
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
		if (dummyClient != null)
			dummyClient.closeConnection("Endpoint closed connection.");
		UDPEndpointHelper.removeEndpoint(instance);
		connected = false;
		loopThread.interrupt();
		receiveThread.interrupt();
		sendThread.interrupt();
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
	public void sendStatus(byte status, byte payload) {
		byte[] buffer = new byte[2];
		buffer[0] = status;
		buffer[1] = payload;
		DatagramPacket errorPacket = new DatagramPacket(buffer, 2, udpAddress, udpPort);
		UDPCommunicator.instance().sendPacketBack(this, errorPacket);
	}

	/**
	 * This is called when the full Packet arrived and we can start processing it.
	 */
	public void processFullPacket(PacketData packetData) {
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
				payLoad = packetData.data[1];
			} else {
				throw new PacketException("Recieved illegal status");
			}
			synchronized (inBlock) {
				inBlock.notify();
			}
			synchronized (outBlock) {
				outBlock.notify();
			}
			return;
		}
		packetData.removeBytes(1);

		// decode the packetNumber
		// We already processed this packet
		if (previousIncomingPacketNumber == packetData.data[0]) {
			Logger.logWarning("Already received this packet", UDPEndpoint.class);
			incomingSubPacketNumber = 0;
			incomingPacket = null;
			sendStatus((byte) 1, previousIncomingPacketNumber);
		}
		if (currentIncomingPacketNumber != packetData.data[0])
			throw new PacketException("Recieved unexpected packet");
		packetData.removeBytes(1);

		short currentSubPacket = PacketInDecoder.decodeShort(packetData);
		short totalSubPackets = PacketInDecoder.decodeShort(packetData);

		if (incomingPacket == null)
			incomingPacket = new byte[totalSubPackets * 506];

		if (Constants.NETWORKDEBUG)
			Logger.logInfo("SubPackage " + currentSubPacket + "/" + totalSubPackets, UDPEndpoint.class);

		incomingSubPacketNumber++;
		System.arraycopy(packetData.data, 0, incomingPacket, (currentSubPacket - 1) * 506, 506);
		if (incomingSubPacketNumber == totalSubPackets) {
			PacketData pData = new PacketData();
			pData.appendBytes(incomingPacket);
			incomingPacket = null;
			incomingPacketQueue.add(pData);
			previousIncomingPacketNumber = currentIncomingPacketNumber;
			currentIncomingPacketNumber++;
			if (currentIncomingPacketNumber > 100)
				currentIncomingPacketNumber = 1;
			incomingSubPacketNumber = 0;
			sendStatus((byte) 1, previousIncomingPacketNumber);
		}
		synchronized (inBlock) {
			inBlock.notify();
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
		if (dummyClient == null || !dummyClient.connected)
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
		synchronized (outBlock) {
			outBlock.notify();
		}
	}
}
