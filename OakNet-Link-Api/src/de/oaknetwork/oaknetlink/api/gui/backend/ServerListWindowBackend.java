package de.oaknetwork.oaknetlink.api.gui.backend;

import de.oaknetwork.oaknetlink.api.gui.structure.ServerListWindow;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.client.CRequestServerListPacket;

/**
 * Interaction logic for the ServerListWindow
 * 
 * @author Fabian Fila
 */
public class ServerListWindowBackend {
	
	private static ServerListWindow window;
	
	public static void setWindow(ServerListWindow window) {
		ServerListWindowBackend.window = window;
	}
	
	/**
	 * this requests the currently running games from the MasterServer
	 */
	public static void refreshServerList() {
		window.serverSelectionList.clear();
		CRequestServerListPacket.sendPacket();
	}
	
	/**
	 * For each running game this method will be called.
	 */
	public static void addServer(String serverName, boolean hasPass, String GameVersion, int users, int maxUsers, byte gameState) {
		
	}

}
