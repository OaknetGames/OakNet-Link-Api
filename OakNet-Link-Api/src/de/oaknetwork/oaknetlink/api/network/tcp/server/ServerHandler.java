package de.oaknetwork.oaknetlink.api.network.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import de.oaknetwork.oaknetlink.api.log.Logger;

public class ServerHandler {

	static ServerHandler instance;

	private ServerSocket serverSocket;

	public ServerHandler(int port) {
		instance = this;
		Logger.logInfo("Create new TCPServerHandler on port: " + port, ServerHandler.class);
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Can't create TCPServerSocket: " + e.getMessage());
			e.printStackTrace(System.err);
		}

		Thread tcpNetworkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Socket clientSocket = serverSocket.accept();
						ClientHelper.addClient(new Client(clientSocket));
					} catch (IOException e) {
						System.err.println("Error while accepting a new connection: " + e.getMessage());
						e.printStackTrace(System.err);
					}
				}
			}
		});
		tcpNetworkThread.start();
	}

	public static ServerHandler instance() {
		return instance;
	}
}
