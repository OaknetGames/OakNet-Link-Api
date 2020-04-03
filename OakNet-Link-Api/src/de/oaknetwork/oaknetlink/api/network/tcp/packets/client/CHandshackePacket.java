package de.oaknetwork.oaknetlink.api.network.tcp.packets.client;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SHandshakeResponsePacket;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;
import de.oaknetwork.oaknetlink.api.utils.AuthHelper;
import de.oaknetwork.oaknetlink.api.utils.Constants;

public class CHandshackePacket extends CPacket{

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("protocolVersion", Short.class);
		expectedTypes.put("userName", String.class);
		expectedTypes.put("uuid", String.class);
		return expectedTypes;
	}
	
	@Override
	public void processPacket(Map<String, Object> data, Client sender) {
		short protocolVersion = (short) data.get("protocolVersion");
		String userName = (String) data.get("userName");
		String uuid = (String) data.get("uuid");
		if(protocolVersion != Constants.ProtocolVersion) {
			sender.disconnect("Outdated mod version, try updating OakNet-Link!");
			return;
		}
		if(!AuthHelper.authenticated(userName, uuid)) {
			sender.disconnect("Not authentificated with the Mojang Servers!");
			return;
		}
		sender.name = userName;
		sender.uuid = uuid;
		Logger.logInfo(userName + " logged in.", CHandshackePacket.class);
		SHandshakeResponsePacket.sendPacket(sender);
	}
	
	public static void sendPacket() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("protocolVersion", Constants.ProtocolVersion);
		data.put("userName", MinecraftHooks.mcInterface.userName());
		data.put("uuid", MinecraftHooks.mcInterface.uuid());
		sendPacket(CHandshackePacket.class, data);
	}
}
