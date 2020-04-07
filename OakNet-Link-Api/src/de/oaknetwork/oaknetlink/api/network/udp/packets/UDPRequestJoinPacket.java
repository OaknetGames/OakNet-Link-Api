package de.oaknetwork.oaknetlink.api.network.udp.packets;

import java.net.Inet4Address;
import java.util.HashMap;
import java.util.Map;

import de.oaknetwork.oaknetlink.api.game.Game;
import de.oaknetwork.oaknetlink.api.game.GameHelper;
import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpointHelper;

/**
 * Using this packet, the client asks for a connection to the given Game.
 * 
 * It provides a gameName and optional a password.
 * 
 * @author Fabian Fila
 */
public class UDPRequestJoinPacket extends UDPPacket {

	@Override
	public Map<String, Class<?>> expectedTypes() {
		Map<String, Class<?>> expectedTypes = new HashMap<String, Class<?>>();
		expectedTypes.put("gameName", String.class);
		expectedTypes.put("password", String.class);
		return expectedTypes;
	}

	@Override
	protected void processPacket(Map<String, Object> data, UDPEndpoint sender) {
		String gameName = (String) data.get("gameName");
		Game game = GameHelper.gameByName(gameName);
		if (game == null) {
			UDPDeclinedJoinPacket.sendPacket(sender, "Game not found!");
			return;
		}
		if (!game.password.equals("")) {
			String password = (String) data.get("Password");
			if (!game.password.equals(password)) {
				UDPDeclinedJoinPacket.sendPacket(sender, "Wrong password!");
				return;
			}
		}
		Logger.logInfo(sender.userName + " connects to " + game.owner.name, UDPRequestJoinPacket.class);
		if (UDPEndpointHelper.endpointByClient(game.owner) == sender) {
			UDPDeclinedJoinPacket.sendPacket(sender, "You cannot connect to your own game!");
			return;
		}
		if (UDPEndpointHelper.endpointByClient(game.owner) == null) {
			UDPDeclinedJoinPacket.sendPacket(sender, "Peer is not connected!");
			return;
		}
		boolean senderIPv4 = sender.udpAddress() instanceof Inet4Address;
		boolean hostIPv4 = UDPEndpointHelper.endpointByClient(game.owner).udpAddress() instanceof Inet4Address;
		if (senderIPv4 && !hostIPv4) {
			UDPDeclinedJoinPacket.sendPacket(sender, "You are connected via IPv4, but the host is using IPv6. \nTry to disable \"Force IPv4\" or ask the Host to enable it!");
			return;
		}
		if (!senderIPv4 && hostIPv4) {
			UDPDeclinedJoinPacket.sendPacket(sender, "You are connected via IPv6, but the host is using IPv4. \nTry to enable \"Force IPv4\" or ask the Host to disable it!");
			return;
		}
		UDPEstablishTunnelPacket.sendPacket(UDPEndpointHelper.endpointByClient(game.owner), sender.udpAddress().getHostAddress(),
				sender.udpPort());
		UDPEstablishTunnelPacket.sendPacket(sender, UDPEndpointHelper.endpointByClient(game.owner).udpAddress().getHostAddress(),
				UDPEndpointHelper.endpointByClient(game.owner).udpPort());
	}

	public static void sendPacket(UDPEndpoint receiver, String gameName, String password) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gameName", gameName);
		data.put("password", password);
		sendPacket(UDPRequestJoinPacket.class, receiver, data);
	}
}
