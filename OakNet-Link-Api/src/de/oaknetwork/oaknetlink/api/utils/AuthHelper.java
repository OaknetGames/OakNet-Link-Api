package de.oaknetwork.oaknetlink.api.utils;

import de.oaknetwork.oaknetlink.api.log.Logger;

/**
 * This class helps to authenticate the User against the Mojang Server.
 * 
 * We don't like crackers here...
 * 
 * @author Fabian Fila
 */
public class AuthHelper {

	/**
	 * This method returns if the given username and uuid combination is
	 * authenticated with the MojangServer
	 * 
	 * @param userName
	 * @param uuid
	 * @return
	 */
	public static boolean authenticated(String userName, String uuid) {
		String[] response;
		try {
			response = HttpUtils.sendGet("https://api.mojang.com/users/profiles/minecraft/" + userName);
		} catch (Exception e) {
			Logger.logException("Can't contact Auth-Server", e, AuthHelper.class);
			return false;
		}
		if (Integer.parseInt(response[0]) == 204 || !(response[1].contains(uuid.replaceAll("-", "")))) {
			return false;
		} else {
			return true;
		}
	}

}
