package de.oaknetwork.oaknetlink.api.network.udp.packets;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.game.GameHelper;
import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.network.tcp.server.ClientHelper;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;
import de.oaknetwork.oaknetlink.api.utils.AuthHelper;
import de.oaknetwork.oaknetlink.api.utils.Constants;

/**
 * This packet is sent to greet the other Endpoint.
 * 
 * There is no response to this Packet except if the user is not authenticated.
 * 
 * @author Fabian Fila
 */
public class UDPHandshakePacket extends UDPPacket {

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("protocolVersion", Short.class);
		expectedTypes.put("userName", String.class);
		expectedTypes.put("uuid", String.class);
		return expectedTypes;
	}

	@Override
	protected void processPacket(Map<String, Object> data, UDPEndpoint sender) {
		short protocolVersion = (short) data.get("protocolVersion");
		String userName = (String) data.get("userName");
		String uuid = (String) data.get("uuid");
		if (protocolVersion != Constants.ProtocolVersion) {
			sender.disconnect("Outdated mod version, try updating OakNet-Link!");
			return;
		}
		if (!AuthHelper.authenticated(userName, uuid)) {
			sender.disconnect("Not authentificated with the Mojang Servers!");
			return;
		}
		sender.userName = userName;
		sender.uuid = uuid;
		Logger.logInfo(userName + " logged in.", UDPHandshakePacket.class);
		
		if (ClientHelper.clientByUDPEndpoint(sender) != null
				&& GameHelper.gameByOwner(ClientHelper.clientByUDPEndpoint(sender)) != null
				&& GameHelper.gameByOwner(ClientHelper.clientByUDPEndpoint(sender)).gameState == 1) {
			GameHelper.gameByOwner(ClientHelper.clientByUDPEndpoint(sender)).gameState = 2;
		}

	}

	public static void sendPacket(UDPEndpoint receiver) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("protocolVersion", Constants.ProtocolVersion);
		data.put("userName", MinecraftHooks.mcInterface.userName());
		data.put("uuid", MinecraftHooks.mcInterface.userName());
		sendPacket(UDPHandshakePacket.class, receiver, data);
	}

}
