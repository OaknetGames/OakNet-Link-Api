package de.oaknetwork.oaknetlink.masterserver;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.network.udp.UDPCommunicator;
import de.oaknetwork.oaknetlink.api.utils.Constants;
import de.oaknetwork.oaknetlink.masterserver.network.tcp.TCPServerHandler;

public class Main {
	
	public static final String VERSION="DEV-1.0.0";

	public static void main(String[] args) {
		Logger.addLogProvider(new ConsoleLogProvider());
		Logger.setStandardLogProvider(Logger.logProvider(ConsoleLogProvider.class));
		
		if(args.length>0&&args[0].equalsIgnoreCase("nogui"))
			Logger.logInfo("Starting in nogui mode", Main.class);
		else
			Console.open();
		Logger.logInfo("Starting MasterServer Version: "+ VERSION + " using API: " + Constants.APIVERSION, Main.class);
		
		new TCPServerHandler(1354);
		new UDPCommunicator(1355);
		
	}
	
}
