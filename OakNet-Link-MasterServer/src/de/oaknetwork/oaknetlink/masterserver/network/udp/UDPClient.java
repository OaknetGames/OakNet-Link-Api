package de.oaknetwork.oaknetlink.masterserver.network.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.network.PacketException;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPPacket;
import de.oaknetwork.oaknetlink.api.network.utils.PacketInDecoder;

public class UDPClient {

	private InetAddress udpAdress;
	private int udpPort;
	private boolean connected = true;

	// incoming
	private long timeSinceLastPacketIn;
	private short currentPacketNumber = -1;
	private short totalSubPacketNumber = 1;
	private Map<Short, List<Byte>> incomingPacketQueue = new HashMap<Short, List<Byte>>();

	// outgoing
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
					if (System.currentTimeMillis() - timeSinceLastPacketIn % 1000 < 10) {
						if (resend && status == 0)
							sendPacket(outgoingPacketQueue.get(0));
						if (resend && currentPacketNumber != -1) {
							// TODO send ERROR back

						}
						resend = false;
					} else
						resend = true;

					if (System.currentTimeMillis() - timeSinceLastPacketIn % 15000 < 10) {
						// TODO timeout
					}
					if (status == 2) {
						sendPacket(outgoingPacketQueue.get(0));
					}
					if (status == 1) {
						outgoingPacketQueue.remove(0);
						status = -1;
					}
					if (status == -1) {
						sendPacket(outgoingPacketQueue.get(0));
					}
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

	private void sendPacket(UDPPacket packetToSendPack) {
		status = 0;

		// TODO SendPacket
	}

	public void processFullPacket() {
		List<Byte> fullPacket = new ArrayList<Byte>();
		for (short i = 1; i <= totalSubPacketNumber; i++) {
			fullPacket.addAll(incomingPacketQueue.get(i));
		}
		// TODO Send OK
		currentPacketNumber = -1;
		totalSubPacketNumber = -1;
		// TODO Process Packet
	}

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
		if (currentPacketNumber == -1)
			currentPacketNumber = packetData.get(0);
		if (currentPacketNumber != packetData.get(0))
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
