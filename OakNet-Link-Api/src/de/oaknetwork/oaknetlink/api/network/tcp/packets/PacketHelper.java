package de.oaknetwork.oaknetlink.api.network.tcp.packets;

import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CDisconnectionPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CGameCreationPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CHandshackePacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CPingPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CRequestServerListPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CUpdateGamePacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SDisconnectionPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SGameCreationResponsePacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SHandshakeResponsePacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SPongPacket;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SRequestServerListResponsePacket;

/**
 * This Class provides some useful methods regarding TCP Packets
 * 
 * @author Fabian Fila
 */
public class PacketHelper {

	/**
	 * Calling this will register all existing TCPPackets, it needs to be called at
	 * init.
	 */
	public static void registerPackets() {
		new CPingPacket();
		new SPongPacket();
		new CDisconnectionPacket();
		new SDisconnectionPacket();
		new CHandshackePacket();
		new SHandshakeResponsePacket();
		new CGameCreationPacket();
		new SGameCreationResponsePacket();
		new CRequestServerListPacket();
		new SRequestServerListResponsePacket();
		new CUpdateGamePacket();
	}
}
