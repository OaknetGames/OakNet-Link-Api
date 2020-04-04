package de.oaknetwork.oaknetlink.api.game;

import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.server.SGameCreationResponsePacket;
import de.oaknetwork.oaknetlink.api.network.tcp.server.Client;

/**
 * This class manages Game related stuff
 * 
 * @author Fabian Fila
 */
public class GameHelper {

	public static ArrayList<Game> runningGames = new ArrayList<Game>();
	
	public static void addGame(Client owner, String gameName, String password, String gameVersion, int maxUsers) {
		if(gameByOwner(owner) != null) {
			Logger.logWarning(owner.name + " tried to register a Server, but they already owned one.", GameHelper.class);
			SGameCreationResponsePacket.sendPacket((byte) 1, "You already own a sever, try restarting your game.", owner);
			return;
		}
		if(gameByName(gameName) != null) {
			Logger.logWarning(owner.name + " tried to register a Server, but the name is already in use.", GameHelper.class);
			SGameCreationResponsePacket.sendPacket((byte) 1, "This servername is already used, choose another one.", owner);
			return;
		}
		Game newGame = new Game(owner, gameName, password, gameVersion, maxUsers);
		runningGames.add(newGame);
		Logger.logInfo(owner.name + " created a new game: " + gameName, GameHelper.class);
		SGameCreationResponsePacket.sendPacket((byte) 0, "OK", owner);
	}
	
	/**
	 * Receives a game by its name.
	 * 
	 * Returns null if no game is found.
	 * 
	 * @param gameName
	 * @return
	 */
	public static Game gameByName(String gameName) {
		Game result = null;
		for(Game game:runningGames) {
			if(game.gameName.equals(gameName)){
				result = game;
			}
		}
		return result;
	}
	
	/**
	 * Receives a game by its owner.
	 * 
	 * Returns null if no game is found.
	 * 
	 * @param gameName
	 * @return
	 */
	public static Game gameByOwner(Client owner) {
		Game result = null;
		for(Game game:runningGames) {
			if(game.owner.equals(owner)){
				result = game;
			}
		}
		return result;
	}
}
