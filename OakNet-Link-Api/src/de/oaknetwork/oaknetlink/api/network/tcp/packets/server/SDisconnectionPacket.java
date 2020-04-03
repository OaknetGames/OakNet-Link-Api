package de.oaknetwork.oaknetlink.api.network.tcp.packets.server;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.network.tcp.client.ClientHandler;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This Packet is sent by the Server when it closes the Connection to client
 * 
 * @author Fabian Fila
 */
public class SDisconnectionPacket extends SPacket{

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("reason", String.class);
		return expectedTypes;
	}
	@Override
	public void processPacket(Map<String, Object> data) {
		String reason = (String) data.get("reason");
		ClientHandler.closeConnection(reason);	
	}
	
	public static void sendPacket(String reason, Client receiver) {
		Map<String, Object> data= new HashMap<String, Object>();
		data.put("reason", reason);
		sendPacket(SDisconnectionPacket.class, receiver, data);
	}

}
