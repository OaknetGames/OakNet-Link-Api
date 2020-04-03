package de.oaknetwork.oaknetlink.api.mcinterface;

import de.oaknetwork.oaknetlink.api.gui.GuiManager;
import de.oaknetwork.oaknetlink.api.log.LogWindow;
import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.log.MinecraftLogProvider;
import de.oaknetwork.oaknetlink.api.log.MinecraftServerLogProvider;
import de.oaknetwork.oaknetlink.api.log.OakNetLinkLogProvider;
import de.oaknetwork.oaknetlink.api.network.tcp.client.ClientHandler;
import de.oaknetwork.oaknetlink.api.network.tcp.packets.PacketHelper;
import de.oaknetwork.oaknetlink.api.network.udp.UDPCommunicator;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpoint;
import de.oaknetwork.oaknetlink.api.network.udp.UDPEndpointHelper;
import de.oaknetwork.oaknetlink.api.network.udp.packets.UDPPacketHelper;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * CONCEPT Interfaced Communication
 * 
 * The goal is to break the communication with minecraft to a minimum. To achive
 * this we use two classes as an interface to minecraft.
 * 
 * The mod is only a wrapper which wraps the game's logic to this version
 * independent API
 * 
 * 1. IMinecraft: this Interface needs to be implemented by the Minecraft
 * 
 * 2. Minecraft Hooks: These methods have to be called from the wrapper
 * 
 * @author Fabian Fila
 */
public class MinecraftHooks {

	public static IMinecraft mcInterface;

	public static void preInit(IMinecraft mcInterfaceIn) {
		mcInterface = mcInterfaceIn;
		LogWindow.open();
		Logger.addLogProvider(new MinecraftLogProvider(mcInterface));
		Logger.addLogProvider(new OakNetLinkLogProvider());
		Logger.addLogProvider(new MinecraftServerLogProvider());
		Logger.setDefaultLogProvider(Logger.logProvider(OakNetLinkLogProvider.class));
		
		Logger.logInfo("OakNet-Link is starting...", MinecraftHooks.class, OakNetLinkLogProvider.class, MinecraftLogProvider.class);

	}

	public static void init() {
		// UDP Network
		UDPPacketHelper.registerPackets();
		new UDPCommunicator();
		UDPEndpoint masterServer = UDPEndpointHelper.createMasterServerEndpoint();
		masterServer.debug = true;

		// TCP Network
		PacketHelper.registerPackets();
		ClientHandler.connect();
	}

	public static void postInit() {
		Logger.logInfo("Init GuiManager...", MinecraftHooks.class);
		new GuiManager();
	}

	// 0 = Left click; 1 = Right click; 2 = Mouse wheel click
	public static boolean mouseDown(int mouseX, int mouseY, int mouseButton) {
		return GuiManager.instance().mouseDown(new Vector2i(mouseX, mouseY), mouseButton);
	}

	// 0 = Left click; 1 = Right click; 2 = Mouse wheel click
	public static boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
		return GuiManager.instance().mouseReleased(new Vector2i(mouseX, mouseY), mouseButton);
	}

	public static boolean mouseOver(int mouseX, int mouseY, int mouseWheelDelta) {
		return GuiManager.instance().mouseOver(new Vector2i(mouseX, mouseY), mouseWheelDelta);
	}

	public static boolean keyPressed(char key, int keyCode) {
		return GuiManager.instance().keyPressed(key, keyCode);
	}

	public static void onRender() {
		GuiManager.instance().render(new Vector2i(0, 0));
	}
}
