package de.oaknetwork.oaknetlink.api.network.tcp.packets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.PacketException;
import de.oaknetwork.oaknetlink.api.network.tcp.client.ClientHandler;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPPacket;
import de.oaknetwork.oaknetlink.api.network.utils.BytePackage;
import de.oaknetwork.oaknetlink.api.network.utils.PacketData;
import de.oaknetwork.oaknetlink.api.network.utils.PacketInDecoder;
import de.oaknetwork.oaknetlink.api.network.utils.PacketOutEncoder;
import de.oaknetwork.oaknetlink.api.utils.Constants;

/**
 * CONCEPT TCPPackets
 * 
 * Because this communication is sided, we need subclasses for the server and
 * client side.
 * 
 * TCPPackets need to extend either the SPacket or the CPacket class.
 * 
 * THEY SHOULD NEVER EXTEND THIS CLASS DIRECTLY!!!!111!EINS!!!!11!!!ELF!!!
 * 
 * In order that packets function correctly they need to be instantiated once at
 * initialization.
 * 
 * Usually this is done with the PacketHelper.java
 * 
 * Your Packet needs to provide an expectedTypes Map where you can define the
 * structure of your Packet
 * 
 * e.g.: {"Username", String.class}, {"ID", Integer.class}
 * 
 * This means your packet expects one String and one Integer
 * 
 * The Data will be provided with another Map, but this time the map contains
 * the actual data. Attention the keys need to match!
 * 
 * e.g.: {"Username", "Akinito"}, {"ID", new Integer(25)}
 * 
 * I think the best for implementing new Packets is to look at already existing
 * ones.
 * 
 * @author Fabian Fila
 */
public abstract class Packet {
	
	protected byte packetID;

	protected static ArrayList<Packet> registeredPackets = new ArrayList<Packet>();

	public Packet() {
		boolean isAlreadyRegistered = false;
		for (Packet packet : registeredPackets) {
			isAlreadyRegistered = this.getClass().isInstance(packet) || isAlreadyRegistered;
		}
		packetID = (byte) registeredPackets.size();
		if (isAlreadyRegistered)
			throw new RuntimeException("The Packet: " + this.getClass().getCanonicalName() + " is already registered");
		registeredPackets.add(this);
		Logger.logInfo("Registered new TCPPacket: " + this.getClass().getCanonicalName(), Packet.class);
	}
	
	/**
	 * Calling this method sends out a packet to the given receiver. This should be
	 * wrapped by another static method in the corresponding packet class.
	 * 
	 * @param clazz    the packet which should be sent
	 * @param data     a map containing the data, this has to correspond with the
	 *                 expected types map. This means both maps need the same keys,
	 *                 but instead of providing the types, the data map's values
	 *                 need to be the actual objects.
	 * 
	 */
	public static void sendPacket(Class<?> clazz, Client receiver, Map<String, Object> data) {
		// get the expected packet
		Optional<Packet> result = registeredPackets.stream().filter(element -> clazz.isInstance(element))
				.findFirst();
		if (!result.isPresent())
			throw new RuntimeException("Error while sending packet: Can't find given TCPPacket");
		Packet expectedPacket = (Packet) result.get();

		// check and encode referred data
		Map<String, Class<?>> expectedTypes = expectedPacket.expectedTypes();
		PacketData packetData = new PacketData();
		// add the PacketID
		packetData.appendBytes(expectedPacket.packetID);
		try {
			for (String key : expectedTypes.keySet()) {
				if (!data.containsKey(key))
					throw new RuntimeException(
							"Error while sending packet: The key: " + key + " is missing in the provided dataset");
				Class<?> expectedType = expectedTypes.get(key);
				if (expectedType == Byte.class) {
					packetData.appendBytes((Byte) data.get(key));
				} else if (expectedType == Short.class) {
					PacketOutEncoder.encodeShort(packetData, (Short) data.get(key));
				} else if (expectedType == Integer.class) {
					PacketOutEncoder.encodeInt(packetData, (Integer) data.get(key));
				} else if (expectedType == Long.class) {
					PacketOutEncoder.encodeLong(packetData, (Long) data.get(key));
				} else if (expectedType == String.class) {
					PacketOutEncoder.encodeString(packetData, (String) data.get(key));
				} else if (expectedType == BytePackage.class) {
					PacketOutEncoder.encodeBytePackage(packetData, (BytePackage) data.get(key));
				} else {
					throw new RuntimeException("Error while sending packet: Expected type unknown");
				}
			}
		} catch (ClassCastException e) {
			throw new RuntimeException(
					"Error while sending packet: Type mismatch, referred object does not belongs to expected Type");
		}
		if(receiver==null)
			ClientHandler.sendPacket(packetData);
		else
			receiver.sendPacket(packetData);
	}
	
	/**
	 * This method should return a Map with the expected type structure of the
	 * packet.
	 *
	 * Possible types are: Byte, Short, Integer, Long, String, BytePackage
	 */
	public abstract Map<String, Class<?>> expectedTypes();

	
	public static void decodePacket(PacketData packetData, Client sender) throws PacketException {
		// Get PacketID
		byte packetId = packetData.data[0];
		packetData.removeBytes(1);

		// Find Packet
		Packet receivedPacket = null;
		for (Packet packet : registeredPackets) {
			if (packet.packetID == packetId) {
				receivedPacket = packet;
				break;
			}
		}
		if (receivedPacket == null)
			throw new PacketException("Error while receiving UDPPacket: Received PacketID is not registered.");

		if (Constants.NETWORKDEBUG)
			Logger.logInfo("Received new Packet: " + receivedPacket.getClass().getSimpleName(), UDPPacket.class);

		// Fill Data
		HashMap<String, Object> data = new HashMap<String, Object>();
		Map<String, Class<?>> expectedTypes = receivedPacket.expectedTypes();

		for (String key : expectedTypes.keySet()) {
			Class<?> expectedType = expectedTypes.get(key);
			if (expectedType == Byte.class) {
				data.put(key, packetData.data[0]);
				packetData.removeBytes(1);
			} else if (expectedType == Short.class) {
				data.put(key, new Short(PacketInDecoder.decodeShort(packetData)));
			} else if (expectedType == Integer.class) {
				data.put(key, new Integer(PacketInDecoder.decodeInt(packetData)));
			} else if (expectedType == Long.class) {
				data.put(key, new Long(PacketInDecoder.decodeLong(packetData)));
			} else if (expectedType == String.class) {
				data.put(key, PacketInDecoder.decodeInt(packetData));
			} else if (expectedType == BytePackage.class) {
				data.put(key, PacketInDecoder.decodeBytePackage(packetData));
			} else {
				throw new RuntimeException("Error while receiving UDPPacket: Expected type unknown");
			}
		}

		// Process Packet
		if(sender==null)
			((SPacket)receivedPacket).processPacket(data);
		else
			((CPacket)receivedPacket).processPacket(data, sender);
	}
}
