package de.oaknetwork.oaknetlink.masterserver;

import de.oaknetwork.oaknetlink.api.utils.Constants;
import de.oaknetwork.oaknetlink.masterserver.network.tcp.TCPServerHandler;
import de.oaknetwork.oaknetlink.masterserver.network.udp.UDPCommunicator;

public class Main {
	
	public static final String VERSION="DEV-1.0.0";

	public static void main(String[] args) {
		if(args.length>0&&args[0].equalsIgnoreCase("nogui"))
			System.out.println("Start in nogui mode");
		else
			Console.open();
		System.out.println("Starting MasterServer Version: "+ VERSION + " using API: " + Constants.APIVERSION);
		
		new TCPServerHandler(1354);
		new UDPCommunicator(1355);
		
	}
	
}
