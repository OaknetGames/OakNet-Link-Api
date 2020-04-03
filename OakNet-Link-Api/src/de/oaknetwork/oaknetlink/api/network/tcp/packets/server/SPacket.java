package de.oaknetwork.oaknetlink.api.network.tcp.packets.server;

import java.util.Map;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.Packet;

/**
 * This class represents a packet which is sent by the server to the client.
 * 
 * Each client packet needs to extend this class.
 * 
 * @author Fabian Fila
 */
public abstract class SPacket extends Packet {

	/**
	 * This method is called when the Packet is received.
	 * 
	 * @param data   the received already decoded corresponding to the expectedTypes
	 */
	public abstract void processPacket(Map<String, Object> data);

}
