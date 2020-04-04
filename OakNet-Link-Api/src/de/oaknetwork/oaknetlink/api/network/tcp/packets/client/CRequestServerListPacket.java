package de.oaknetwork.oaknetlink.api.network.tcp.packets.client;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.game.Game;
import de.oaknetwork.oaknetlink.api.game.GameHelper;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SRequestServerListResponsePacket;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This packet asks the server for the running games list
 * 
 * @author Fabian Fila
 */
public class CRequestServerListPacket extends CPacket{

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		return expectedTypes;
	}

	@Override
	public void processPacket(Map<String, Object> data, Client sender) {
		for(Game runningGame : GameHelper.runningGames) {
			SRequestServerListResponsePacket.sendPacket(sender, runningGame);
		}
		
	}
	
	public static void sendPacket() {
		Map<String, Object> data = new HashMap<String, Object>();
		sendPacket(CRequestServerListPacket.class, data);
	}

}
