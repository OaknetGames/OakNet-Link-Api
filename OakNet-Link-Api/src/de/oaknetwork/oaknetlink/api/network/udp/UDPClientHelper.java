package de.oaknetwork.oaknetlink.api.network.udp;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import de.oaknetwork.oaknetlink.api.log.Logger;

public class UDPClientHelper {
	
	private static List<UDPClient> connectedClients = new ArrayList<UDPClient>();
	
	public static UDPClient getClientByPacket(DatagramPacket dpacket) {
		UDPClient result = null;
		for(UDPClient client:connectedClients) {
			if(client.udpAdress()==dpacket.getAddress()&&client.udpPort()==dpacket.getPort())
				result=client;
		}
		if(result==null) {
			result = new UDPClient(dpacket.getAddress(), dpacket.getPort());
			connectedClients.add(result);
			Logger.logInfo(result.udpAdress().toString() + " connected via UDP.", UDPClientHelper.class);		
		}
		return result;
	}

}
