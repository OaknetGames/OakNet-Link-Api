package de.oaknetwork.oaknetlink.api.game;

import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * Each running server on the network is represented by this class
 * 
 * @author Fabian Fila
 */
public class Game {

	public Client owner;
	public String gameName;
	public String password;
	public String gameVersion;
	public int currentUsers;
	public int maxUsers;
	
	/**
	 * 0 = Starting, 1 = Waiting for UDP conneection, 2 = Running;
	 */
	public byte gameState = 0;
	
	/**
	 * Creates a new game
	 */
	public Game(Client owner, String gameName, String password, String gameVersion, int maxUsers) {
		this.owner = owner;
		this.gameName = gameName;
		this.password = password;
		this.gameVersion = gameVersion;
		this.maxUsers = maxUsers;
	}

	public void closeGame() {
		GameHelper.removeGame(this);
		
	}
	
}
