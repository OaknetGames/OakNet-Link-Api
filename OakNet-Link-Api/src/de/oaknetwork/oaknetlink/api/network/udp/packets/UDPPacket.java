package de.oaknetwork.oaknetlink.api.network.udp.packets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.network.udp.UDPClient;

/**
 * CONCEPT UDPPackets 
 * 
 * UDPPackets need to extend this Class. In order that
 * packets function correctly they need to be instantiated once at
 * initialization.
 * 
 * E.g. You create a HandshakePacket extends UDPPacket You need to create an
 * instance with new HandshakePacket() at init.
 * 
 * Your Packet needs to override two methods: 1. processPacket: this method is
 * called when this Packet is received 2. preparePacket: this method is called
 * when the Packet will be sent
 * 
 * @author Fabian Fila
 */
public abstract class UDPPacket {

	private byte packetID;

	static ArrayList<UDPPacket> registeredPackets = new ArrayList<UDPPacket>();

	public UDPPacket() {

	}

	/**
	 * This method is called when the Packet is received.
	 * 
	 * @param PacketData the received PacketData ready to be decoded.
	 */
	protected abstract void processPacket(List<Byte> PacketData);

	/**
	 * This method is called when the packet will be sent.
	 * 
	 * @param params the parameters which are passed to the Packet e.g. a username
	 *               for a HandshakePacket
	 * @return a List containing the packetData
	 */
	protected abstract List<Byte> preparePacket(Object... params);

	/**
	 * Calling this method sends out a packet to the given receiver
	 * 
	 * @param clazz    the packet which should be sent
	 * @param reciever the UDPClient who should receive this packet
	 * @param params   the parameters which are passed to the Packet e.g. a username
	 *                 for a HandshakePacket
	 */
	public static void sendPacket(Class clazz, UDPClient receiver, Object... params) {

	}

}
