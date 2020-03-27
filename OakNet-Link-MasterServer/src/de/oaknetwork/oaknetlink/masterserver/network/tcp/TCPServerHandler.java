package de.oaknetwork.oaknetlink.masterserver.network.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.log.Logger;

public class TCPServerHandler {
	
	static TCPServerHandler instance;
	
	private ServerSocket serverSocket;
	
	public TCPServerHandler(int port) {
		instance=this;
		Logger.logInfo("Create new TCPServerHandler on port: " + port, TCPServerHandler.class);
		System.out.println();
		try {
			serverSocket=new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Can't create TCPServerSocket: " + e.getMessage());
			e.printStackTrace(System.err);
		}
		
		Thread tcpNetworkThread= new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						Socket clientSocket=serverSocket.accept();
						TCPClientHelper.addClient(new TCPClient(clientSocket));
					} catch (IOException e) {
						System.err.println("Error while accepting a new connection: " + e.getMessage());
						e.printStackTrace(System.err);
					} 
				}
			}
		});
		tcpNetworkThread.start();
	}
	
	public static TCPServerHandler instance() {
		return instance;
	}
}
