package de.oaknetwork.oaknetlink.api.network.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.network.PacketException;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPPacket;
import de.oaknetwork.oaknetlink.api.network.utils.PacketInDecoder;

/**
 * This represents an endpoint in the UDP Communication
 * 
 * @author Fabian Fila
 */
public class UDPClient {

	private InetAddress udpAdress;
	private int udpPort;
	private boolean connected = true;

	// incoming
	private long timeSinceLastPacketIn;
	private short currentIncomingPacketNumber = -1;
	private short totalSubPacketNumber = 1;
	private Map<Short, List<Byte>> incomingPacketQueue = new HashMap<Short, List<Byte>>();

	// outgoing
	private byte outgoingPacketNumber = 1;
	// Status -1 = nothing, 0 = waiting; 1 = OK; 2 = ERROR
	private byte status = -1;
	private List<UDPPacket> outgoingPacketQueue = new ArrayList<UDPPacket>();

	public UDPClient(InetAddress iaddress, int port) {
		this.udpAdress = iaddress;
		this.udpPort = port;
		Thread clientThread = new Thread(new Runnable() {

			@Override
			public void run() {
				boolean resend = true;
				while (connected) {
					// Every second
					if (System.currentTimeMillis() - timeSinceLastPacketIn % 1000 < 10) {
						// If we sent a packet and wait for an answer, it probably didn't arrive, so we
						// resent it.
						if (resend && status == 0)
							sendPacket(outgoingPacketQueue.get(0));
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
					if (System.currentTimeMillis() - timeSinceLastPacketIn % 15000 < 10) {
						// TODO timeout
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
					// Sleep to not waste all the CPU time
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
	public void sendPacket(UDPPacket packetToSend) {
		status = 0;

		// TODO SendPacket
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
		DatagramPacket errorPacket = new DatagramPacket(buffer, 1, udpAdress, udpPort);
		UDPCommunicator.instance().sendPacketBack(this, errorPacket);
	}

	/**
	 * This is called when the full Packet arrived and we can start processing it.
	 */
	public void processFullPacket() {
		List<Byte> fullPacket = new ArrayList<Byte>();
		for (short i = 1; i <= totalSubPacketNumber; i++) {
			fullPacket.addAll(incomingPacketQueue.get(i));
		}
		sendStatus((byte) 1);
		currentIncomingPacketNumber = -1;
		totalSubPacketNumber = -1;
		incomingPacketQueue.clear();
		// TODO Process Packet
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
		List<Byte> packetData = new ArrayList<Byte>();
		for (Byte packetByte : dpacket.getData()) {
			packetData.add(packetByte);
		}
		if (packetData.get(0) != 0) {
			if (packetData.get(0) == 1 || packetData.get(0) == 2) {
				status = packetData.get(0);
			} else {
				throw new PacketException("Recieved illegal status");
			}
			return;
		}
		packetData.remove(0);
		if (currentIncomingPacketNumber == -1)
			currentIncomingPacketNumber = packetData.get(0);
		if (currentIncomingPacketNumber != packetData.get(0))
			throw new PacketException("Recieved unexpected packet");
		packetData.remove(0);
		short currentSubPacket = PacketInDecoder.decodeShort(packetData);
		short totalSubPackets = PacketInDecoder.decodeShort(packetData);
		totalSubPacketNumber = totalSubPackets;
		incomingPacketQueue.put(currentSubPacket, new ArrayList<Byte>(packetData));
		if (incomingPacketQueue.size() == totalSubPackets)
			processFullPacket();

	}

	public InetAddress udpAdress() {
		return udpAdress;
	}

	public int udpPort() {
		return udpPort;
	}
}
