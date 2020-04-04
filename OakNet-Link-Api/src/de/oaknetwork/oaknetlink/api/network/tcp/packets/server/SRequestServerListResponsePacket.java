package de.oaknetwork.oaknetlink.api.network.tcp.packets.server;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.game.Game;
import de.oaknetwork.oaknetlink.api.gui.backend.ServerListWindowBackend;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This packet is sent in response to the ServerListRequest packet it contains a
 * list of all running games.
 * 
 * There will be one packet for each running server
 * 
 * @author Fabian Fila
 */
public class SRequestServerListResponsePacket extends SPacket {

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("gameName", String.class);
		expectedTypes.put("hasPass", Byte.class);
		expectedTypes.put("gameVersion", String.class);
		expectedTypes.put("currentPlayers", Short.class);
		expectedTypes.put("maxPlayers", Short.class);
		expectedTypes.put("gameState", Byte.class);
		return expectedTypes;
	}
	
	@Override
	public void processPacket(Map<String, Object> data) {
		String serverName = (String) data.get("serverName");
		boolean hasPass = (byte) data.get("hasPass") == (byte) 1;
		String gameVersion = (String) data.get("gameVersion");
		short users = (short) data.get("currentPlayers");
		short maxUsers = (short) data.get("maxPlayers");
		byte gameState = (byte) data.get("serverName");
		ServerListWindowBackend.addServer(serverName, hasPass, gameVersion, users, maxUsers, gameState);
		
	}
	
	public static void sendPacket(Client receiver, Game game) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gameName", game.gameName);
		data.put("hasPass", game.password==""? (byte) 0 : (byte) 1);
		data.put("gameVersion", game.gameVersion);
		data.put("currentPlayers", game.currentUsers);
		data.put("maxPlayers", game.maxUsers);
		data.put("gameState", game.gameState);
		sendPacket(SRequestServerListResponsePacket.class, receiver, data);
	}

}
