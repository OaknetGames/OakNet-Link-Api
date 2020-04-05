package de.oaknetwork.oaknetlink.api.network.tcp.packets.server;

import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This Packet is the response to a PingPacket and contains the timestamp which
 * was received in said Packet
 * 
 * @author Fabian Fila
 */
public class SPongPacket extends SPacket{

		@Override
		public Map<String, Class<?>> expectedTypes() {
			Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
			expectedTypes.put("timestamp", Long.class);
			return expectedTypes;
		}

		@Override
		public void processPacket(Map<String, Object> data) {
			long timestamp = (long) data.get("timestamp");
			int ping = (int) (System.currentTimeMillis() - timestamp);
			//Logger.logInfo("Ping: " + ping, SPongPacket.class);
		}

		public static void sendPacket(Client receiver, long timestamp) {
			// Here we provide the real data
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("timestamp", timestamp);
			sendPacket(SPongPacket.class, receiver, data);
		}
}
