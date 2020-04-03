package de.oaknetwork.oaknetlink.api.network.tcp.packets.server;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

public class SHandshakeResponsePacket extends SPacket{

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("permissionLevel", Byte.class);
		return expectedTypes;
	}
	
	@Override
	public void processPacket(Map<String, Object> data) {
		// TODO Add Permission Stuff
		
	}
	
	public static void sendPacket(Client receiver) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("permissionLevel", new Byte((byte) 0));
		sendPacket(SHandshakeResponsePacket.class, receiver, data);		
	}
}
