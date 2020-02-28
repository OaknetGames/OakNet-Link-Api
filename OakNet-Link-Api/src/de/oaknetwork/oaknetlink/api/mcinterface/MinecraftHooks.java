package de.oaknetwork.oaknetlink.api.mcinterface;

import java.awt.Font;
import java.awt.Graphics2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.oaknetwork.oaknetlink.api.gui.GuiManager;
import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.Color;
import de.oaknetwork.oaknetlink.api.gui.components.TextBox;
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
		Button button = new Button(null, new Vector2i(20, 20), new Vector2i(100, 20), "Hallo Welt!", 1, new Runnable() {
			
			@Override
			public void run() {
				Logger.instance().logProvider(OakNetLinkLogProvider.class).logInfo("Button Click!");
				
			}
		});
		GuiManager.addComponent(button);
		
		TextBox textBox = new TextBox(null, new Vector2i(20, 50), new Vector2i(150, 20), 1, "Text");
		GuiManager.addComponent(textBox);
	}
	
	// 0 = Left click; 1 = Right click; 2 = Mouse wheel click
	public static boolean click(int mouseX, int mouseY, int mouseButton) {
		return GuiManager.click(new Vector2i(mouseX, mouseY), mouseButton);
	}
	
	public static boolean mouseOver(int mouseX, int mouseY) {
		return GuiManager.mouseOver(new Vector2i(mouseX, mouseY));
	}
	
	public static boolean keyPressed(char key, int keyCode) {
		return GuiManager.keyPressed(key, keyCode);
	}
	
	public static void onRender() {
		GuiManager.render();
	}
}
