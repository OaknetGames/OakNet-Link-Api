package de.oaknetwork.oaknetlink.api.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class provides some useful constants.
 * 
 * TODO Move this to a Config file
 * 
 * @author Fabian Fila
 */
public class Constants {

	// General
	public static final String APIVERSION = "DEV-2.0.0";
	public static final short ProtocolVersion = 0;
	public static final Path FOLDERPATH = Paths.get("oaknetlink/");
	public static final Path LOGPATH = Paths.get("oaknetlink/log/");
	
	// Network
	public static final boolean NETWORKDEBUG = false;
	public static final String MASTERSERVERADDRESS = "onl.oaknetwork.de";
	public static final int TCPPORT = 1354;
	public static final int UDPPORT = 1355;
}
