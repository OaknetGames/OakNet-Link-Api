package de.oaknetwork.oaknetlink.api.network.tcp.packets.client;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SPongPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This Packet sends the current time to the Server. Its used to calculate the
 * Ping
 * 
 * @author Fabian Fila
 */
public class CPingPacket extends CPacket{

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("timestamp", Long.class);
		return expectedTypes;
	}
	
	@Override
	public void processPacket(Map<String, Object> data, Client sender) {
		long timestamp = (long) data.get("timestamp");
		SPongPacket.sendPacket(sender, timestamp);
		
	}
	
	public static void sendPacket() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("timestamp", System.currentTimeMillis());
		sendPacket(CPingPacket.class, null, data);
	}
}
