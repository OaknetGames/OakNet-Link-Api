package de.oaknetwork.oaknetlink.api.network.tcp.packets.client;

import java.util.Map;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.Packet;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This class represents a packet which is sent by the client to the server.
 * 
 * Each client packet needs to extend this class.
 * 
 * @author Fabian Fila
 */
public abstract class CPacket extends Packet {

	/**
	 * This method is called when the Packet is received.
	 * 
	 * @param data   the received already decoded corresponding to the expectedTypes
	 */
	public abstract void processPacket(Map<String, Object> data, Client sender);

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
	public static void sendPacket(Class<?> clazz, Map<String, Object> data) {
		Packet.sendPacket(clazz,null, data);
	}
}
