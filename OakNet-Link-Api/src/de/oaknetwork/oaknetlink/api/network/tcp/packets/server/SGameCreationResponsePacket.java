package de.oaknetwork.oaknetlink.api.network.tcp.packets.server;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.gui.components.Dialog;
import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This packet is sent as Response to the CGameCreationPacket. It says if the
 * game has been created and if it has not been created it gives a reason.
 * 
 * responses can be: 0: OK, 1: ERROR
 * 
 * @author Fabian Fila
 */
public class SGameCreationResponsePacket extends SPacket {

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("response", Byte.class);
		expectedTypes.put("reason", String.class);
		return expectedTypes;
	}

	@Override
	public void processPacket(Map<String, Object> data) {
		byte response = (byte) data.get("response");
		if(response == 0) {
			// TODO Start Server Creation Process
			return;
		} 
		if(response == 1 ) {
			String reason = (String) data.get("reason");
			Logger.logError("Cant create Server: " + reason, SGameCreationResponsePacket.class);
			new Dialog(1, "Error", "Cant create Server: " + reason, true, true);
			return;
		}
		// Should never be here
		throw new RuntimeException("Received illegal response");
		

	}
	
	public static void sendPacket(byte response, String reason, Client receiver) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("response", response);
		data.put("reason", reason);
		sendPacket(SGameCreationResponsePacket.class, receiver, data);
	}

}
