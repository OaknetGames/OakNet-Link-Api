package de.oaknetwork.oaknetlink.api.network.tcp.packets.client;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This Packet will be sent by the client when it disconnects.
 * 
 * @author Fabian Fila
 */
public class CDisconnectionPacket extends CPacket{

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("reason", String.class);
		return expectedTypes;
	}
	
	@Override
	public void processPacket(Map<String, Object> data, Client sender) {
		String reason = (String) data.get("reason");
		sender.closeConnection(reason);
	}
	
	public static void sendPacket(String reason) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("timestamp", reason);
		sendPacket(CDisconnectionPacket.class, data);
	}

}
