package de.oaknetwork.oaknetlink.api.network.udp.packets;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;

/**
 * This Packet is sent when a endpoint closes its connection.
 * 
 * @author Fabian Fila
 */
public class UDPDisconnectionPacket extends UDPPacket{

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("reason", String.class);
		return expectedTypes;
	}

	@Override
	protected void processPacket(Map<String, Object> data, UDPEndpoint sender) {
		String reason = (String) data.get("reason");
		sender.closeConnection(reason);
		
	}
	
	public static void sendPacket(UDPEndpoint receiver, String reason) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("reason", reason);
		sendPacket(UDPDisconnectionPacket.class, receiver, data);
	}

}
