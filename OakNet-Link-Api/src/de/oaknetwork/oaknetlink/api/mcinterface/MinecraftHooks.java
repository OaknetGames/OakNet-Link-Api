package de.oaknetwork.oaknetlink.api.mcinterface;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

import de.oaknetwork.oaknetlink.api.log.LogWindow;
import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.log.MinecraftLogProvider;
import de.oaknetwork.oaknetlink.api.log.OakNetLinkLogProvider;

//These methods have to be called from the wrapper
public class MinecraftHooks {

	public static IMinecraft mcInterface;
	
	public static void preInit(IMinecraft mcInterfaceIn) {
		mcInterface = mcInterfaceIn;
		LogWindow.open();
		Logger logger = new Logger(mcInterface);
		logger.getLogProvider(MinecraftLogProvider.class).logInfo("OakNet-Link is starting...");
		logger.getLogProvider(OakNetLinkLogProvider.class).logInfo("OakNet-Link is starting...");
	}
	
	public static void init() {
	}
	
	public static void postInit() {
	}
	
	public static void onRender() {
		//mcInterface.drawRect(0, 0, 200, 80, 0xff242424);
		//TrueTypeFont ttf = new TrueTypeFont(new Font("Calibri", Font.PLAIN, 32), true);
		//ttf.drawString(10, 10, "Hallo Welt!");
		// set the color of the quad (R,G,B,A)

	    Renderer.get().glBegin(SGL.GL_QUADS);
		Color.blue.bind(); 
	    Renderer.get().glVertex2f(10, 10);
	    Renderer.get().glVertex2f(10, 100);
	    Renderer.get().glVertex2f(100, 100);
	    Renderer.get().glVertex2f(100, 10);

	    Renderer.get().glEnd();
	}
}
