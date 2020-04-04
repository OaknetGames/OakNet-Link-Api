package de.oaknetwork.oaknetlink.api.network.tcp.packets.client;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.game.GameHelper;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This Packet is sent when the client wants to host a new server.
 * 
 * @author Fabian Fila
 */
public class CGameCreationPacket extends CPacket{

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("gameName", String.class);
		expectedTypes.put("password",String.class);
		expectedTypes.put("gameVersion",String.class);
		expectedTypes.put("maxUsers", Short.class);
		return expectedTypes;
	}

	@Override
	public void processPacket(Map<String, Object> data, Client sender) {
		String gameName = (String) data.get("gameName");
		String password = (String) data.get("password");
		String gameVersion = (String) data.get("gameVersion");
		short maxUsers = (short) data.get("maxUsers");
		GameHelper.addGame(sender, gameName, password, gameVersion, maxUsers);
	}
	
	public static void sendPacket(String gameName, String password, String gameVersion, Short maxUsers) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gameName", gameName);
		data.put("password", password);
		data.put("gameVersion", gameVersion);
		data.put("maxUsers", maxUsers);
		sendPacket(CGameCreationPacket.class, data);
	}

}
