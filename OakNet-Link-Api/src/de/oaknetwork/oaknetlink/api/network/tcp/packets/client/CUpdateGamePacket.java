package de.oaknetwork.oaknetlink.api.network.tcp.packets.client;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.game.GameHelper;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpointHelper;

/**
 * This packet is used to update a game
 * 
 * @author Fabian Fila
 */
public class CUpdateGamePacket extends CPacket {

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("gameName", String.class);
		expectedTypes.put("password", String.class);
		expectedTypes.put("gameState", Byte.class);
		expectedTypes.put("currentUsers", Short.class);
		return expectedTypes;
	}

	@Override
	public void processPacket(Map<String, Object> data, Client sender) {
		String gameName = (String) data.get("gameName");
		if(!gameName.equals("{[~UNCHANGED~]}"))
			GameHelper.gameByOwner(sender).gameName = gameName;
		
		String password = (String) data.get("password");
		if(!password.equals("{[~UNCHANGED~]}"))
			GameHelper.gameByOwner(sender).password = password;
		
		byte gameState = (byte) data.get("gameState");
		if(gameState == 0 || gameState == 1) {
			if(gameState == 1 && UDPEndpointHelper.endpointByClient(sender)!=null)
				gameState = 2;	
			GameHelper.gameByOwner(sender).gameState = gameState;
		}
		
		short currentUsers = (short) data.get("currentUsers");
		if(currentUsers != -1)
			GameHelper.gameByOwner(sender).currentUsers = currentUsers;
	}
	
	public static void sendPacket(String gameName, String password, byte gameState, short currentUsers) {
		if(gameName == null)
			gameName = "{[~UNCHANGED~]}";
		if(password == null)
			password = "{[~UNCHANGED~]}";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gameName", gameName);
		data.put("password", password);
		data.put("gameState", gameState);
		data.put("currentUsers", currentUsers);
		sendPacket(CUpdateGamePacket.class, data);
	}
	
	
}
