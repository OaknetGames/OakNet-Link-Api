package de.oaknetwork.oaknetlink.api.network.udp.packets;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;

/**
 * This Packet sends the current time to another Endpoint. Its used to calculate
 * the Ping
 * 
 * @author Fabian Fila
 */
public class UDPPingPacket extends UDPPacket {

	// Packet Structure
	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("timestamp", Long.class);
		return expectedTypes;
	}

	// Receive Packet
	@Override
	protected void processPacket(Map<String, Object> data, UDPEndpoint sender) {
		long timestamp = (long) data.get("timestamp");
		UDPPongPacket.sendPacket(sender, timestamp);
	}

	// Send Packet
	public static void sendPacket(UDPEndpoint receiver) {
		// Here we provide the real data
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("timestamp", System.currentTimeMillis());
		sendPacket(UDPPingPacket.class, receiver, data);
	}
}
