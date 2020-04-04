package de.oaknetwork.oaknetlink.api.network.udp.packets;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.gui.components.Dialog;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpointHelper;

/**
 * This Packet will be sent if the joining of a game has been declined
 * 
 * @author Fabian Fila
 */
public class UDPDeclinedJoinPacket extends UDPPacket{

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("reason", String.class);
		return expectedTypes;
	}

	@Override
	protected void processPacket(Map<String, Object> data, UDPEndpoint sender) {
		String reason = (String) data.get("reason");
		new Dialog(1, "Can't connect to game", "Failed connecting to game: " + reason, true, true);
		UDPEndpointHelper.masterServerEndpoint.disconnect("disconnected");
	}
	
	public static void sendPacket(UDPEndpoint receiver, String reason) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("reason", reason);
		sendPacket(UDPDeclinedJoinPacket.class, receiver, data);
	}

}
