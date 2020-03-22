package de.oaknetwork.oaknetlink.api.mcinterface;

import de.oaknetwork.oaknetlink.api.gui.GuiManager;
import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.CheckBox;
import de.oaknetwork.oaknetlink.api.gui.components.Color;
import de.oaknetwork.oaknetlink.api.gui.components.ComboBox;
import de.oaknetwork.oaknetlink.api.gui.components.ComboBoxItem;
import de.oaknetwork.oaknetlink.api.gui.components.Dialog;
import de.oaknetwork.oaknetlink.api.gui.components.List;
import de.oaknetwork.oaknetlink.api.gui.components.ListEntry;
import de.oaknetwork.oaknetlink.api.gui.components.TextBox;
import de.oaknetwork.oaknetlink.api.gui.components.TextPane;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.log.LogWindow;
import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.log.MinecraftLogProvider;
import de.oaknetwork.oaknetlink.api.log.OakNetLinkLogProvider;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

// These methods have to be called from the wrapper
public class MinecraftHooks {

	public static IMinecraft mcInterface;

	public static void preInit(IMinecraft mcInterfaceIn) {
		mcInterface = mcInterfaceIn;
		LogWindow.open();
		Logger logger = new Logger(mcInterface);
		logger.logProvider(MinecraftLogProvider.class).logInfo("OakNet-Link is starting...");
		logger.logProvider(OakNetLinkLogProvider.class).logInfo("OakNet-Link is starting...");

	}

	public static void init() {

	}

	public static void postInit() {
		Logger.instance().logProvider(OakNetLinkLogProvider.class).logInfo("Init GuiManager...");
		new GuiManager();
	}

	// 0 = Left click; 1 = Right click; 2 = Mouse wheel click
	public static boolean mouseDown(int mouseX, int mouseY, int mouseButton) {
		return GuiManager.getInstance().mouseDown(new Vector2i(mouseX, mouseY), mouseButton);
	}

	// 0 = Left click; 1 = Right click; 2 = Mouse wheel click
	public static boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
		return GuiManager.getInstance().mouseReleased(new Vector2i(mouseX, mouseY), mouseButton);
	}

	public static boolean mouseOver(int mouseX, int mouseY, int mouseWheelDelta) {
		return GuiManager.getInstance().mouseOver(new Vector2i(mouseX, mouseY), mouseWheelDelta);
	}

	public static boolean keyPressed(char key, int keyCode) {
		return GuiManager.getInstance().keyPressed(key, keyCode);
	}

	public static void onRender() {
		GuiManager.getInstance().render(new Vector2i(0, 0));
	}
}
