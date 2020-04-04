package de.oaknetwork.oaknetlink.api.network.udp.packets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.PacketException;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;
import de.oaknetwork.oaknetlink.api.network.utils.BytePackage;
import de.oaknetwork.oaknetlink.api.network.utils.PacketData;
import de.oaknetwork.oaknetlink.api.network.utils.PacketInDecoder;
import de.oaknetwork.oaknetlink.api.network.utils.PacketOutEncoder;
import de.oaknetwork.oaknetlink.api.utils.Constants;

/**
 * CONCEPT UDPPackets
 * 
 * UDPPackets need to extend this Class. In order that packets function
 * correctly they need to be instantiated once at initialization.
 * 
 * E.g. You create a HandshakePacket extends UDPPacket You need to create an
 * instance with new HandshakePacket() at the init phase.
 * 
 * Usually this is done with the UDPPacketHelper.java
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
public abstract class UDPPacket {

	private byte packetID;

	static ArrayList<UDPPacket> registeredPackets = new ArrayList<UDPPacket>();

	public UDPPacket() {
		boolean isAlreadyRegistered = false;
		for (UDPPacket packet : registeredPackets) {
			isAlreadyRegistered = this.getClass().isInstance(packet) || isAlreadyRegistered;
		}
		packetID = (byte) registeredPackets.size();
		if (isAlreadyRegistered)
			throw new RuntimeException("The Packet: " + this.getClass().getCanonicalName() + " is already registered");
		registeredPackets.add(this);
		Logger.logInfo("Registered new UDPPacket: " + this.getClass().getCanonicalName(), UDPPacket.class);
	}

	/**
	 * This method should return a Map with the expected type structure of the
	 * packet.
	 *
	 * Possible types are: Byte, Short, Integer, Long, String, BytePackage
	 */
	public abstract Map<String, Class<?>> expectedTypes();

	/**
	 * This method is called when the Packet is received.
	 * 
	 * @param data the received already decoded corresponding to the expectedTypes
	 * @param sender who has sent the Packet
	 */
	protected abstract void processPacket(Map<String, Object> data, UDPEndpoint sender);

	/**
	 * Calling this method sends out a packet to the given receiver. This should be
	 * wrapped by another static method in the corresponding packet class.
	 * 
	 * @param clazz    the packet which should be sent
	 * @param reciever the UDPClient who should receive this packet
	 * @param data     a map containing the data, this has to correspond with the
	 *                 expected types map. This means both maps need the same keys,
	 *                 but instead of providing the types, the data map's values
	 *                 need to be the actual objects.
	 * 
	 */
	public static void sendPacket(Class<?> clazz, UDPEndpoint receiver, Map<String, Object> data) {
		// get the expected packet
		Optional<UDPPacket> result = registeredPackets.stream().filter(element -> clazz.isInstance(element)).findFirst();
		if (!result.isPresent())
			throw new RuntimeException("Error while sending packet: Can't find given UDPPacket");
		UDPPacket expectedPacket = (UDPPacket) result.get();

		// check and encode referred data
		Map<String, Class<?>> expectedTypes = expectedPacket.expectedTypes();
		PacketData packetData = new PacketData();
		// add the PacketID
		packetData.appendBytes(expectedPacket.packetID);
		try {
			for (String key : expectedTypes.keySet()) {
				if (!data.containsKey(key))
					throw new RuntimeException(
							"Error while sending packet: The key: " + key + " is missing in the prided dataset");
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
		receiver.addToOutgoingQueue(packetData);
	}

	public static void decodePacket(PacketData packetData, UDPEndpoint sender) throws PacketException {
		// Check if packet is valid
		if(packetData.data.length == 0) {
			Logger.logWarning("Received invalid packet, ignoring...", UDPPacket.class);
			return;
		}
		// Get PacketID
		byte packetId = packetData.data[0];
		packetData.removeBytes(1);

		// Find Packet
		UDPPacket receivedPacket = null;
		for (UDPPacket packet : registeredPackets) {
			if (packet.packetID == packetId) {
				receivedPacket = packet;
				break;
			}
		}
		if (receivedPacket == null)
			throw new PacketException("Error while receiving UDPPacket: Received PacketID is not registered.");
		
		if(Constants.NETWORKDEBUG)
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
				data.put(key, PacketInDecoder.decodeString(packetData));
			} else if (expectedType == BytePackage.class) {
				data.put(key, PacketInDecoder.decodeBytePackage(packetData));
			} else {
				throw new RuntimeException("Error while receiving UDPPacket: Expected type unknown");
			}
		}

		// Process Packet
		receivedPacket.processPacket(data, sender);
	}

}
