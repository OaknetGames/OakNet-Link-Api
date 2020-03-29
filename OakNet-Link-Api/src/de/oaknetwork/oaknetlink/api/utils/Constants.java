package de.oaknetwork.oaknetlink.api.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class provides some useful constants.
 * 
 * @author Fabian Fila
 */
public class Constants {

	public static final String APIVERSION = "DEV-2.0.0";
	public static final short ProtocolVersion = 0;
	public static final Path FOLDERPATH = Paths.get("oaknetlink/");
	public static final Path LOGPATH = Paths.get("oaknetlink/log/");
	
	// TODO Move this to config
	public static final boolean NETWORKDEBUG = false;
}
