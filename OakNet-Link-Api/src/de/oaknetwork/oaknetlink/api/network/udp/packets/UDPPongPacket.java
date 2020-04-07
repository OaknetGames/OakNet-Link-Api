package de.oaknetwork.oaknetlink.api.network.udp.packets;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;

/**
 * This Packet is the response to a PingPacket and contains the timestamp which
 * was received in said Packet
 * 
 * @author Fabian Fila
 */
public class UDPPongPacket extends UDPPacket {

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
		int ping = (int) (System.currentTimeMillis() - timestamp);
		//Logger.logInfo("[" + sender.userName + "] Ping: " + ping, UDPPongPacket.class);
	}

	// Send Packet
	public static void sendPacket(UDPEndpoint receiver, long timestamp) {
		// Here we provide the real data
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("timestamp", timestamp);
		sendPacket(UDPPongPacket.class, receiver, data);
	}

}
