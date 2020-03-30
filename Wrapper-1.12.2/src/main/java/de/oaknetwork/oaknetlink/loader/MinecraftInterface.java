package de.oaknetwork.oaknetlink.loader;

import de.oaknetwork.oaknetlink.api.mcinterface.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * This is the implementation of the IMinecraft
 * 
 * It allows the communication from the API to the Game
 * 
 * @author Fabian Fila
 */
public class MinecraftInterface implements IMinecraft {

	@Override
	public void logInfo(String info) {
		Main.getLogger().info(info);
	}

	@Override
	public void logWarning(String warning) {
		Main.getLogger().warn(warning);
	}

	@Override
	public void logError(String error) {
		Main.getLogger().error(error);
	}

	@Override
	public int screenHeight() {
		return Minecraft.getMinecraft().currentScreen.height;
	}

	@Override
	public int screenWidth() {
		return Minecraft.getMinecraft().currentScreen.width;
	}

	@Override
	public void connectToServer(String adress, int port) {
		FMLClientHandler.instance().connectToServer(new GuiMultiplayer(new GuiMainMenu()),
				new ServerData("OakNet-Link Server", adress + ":" + port, false));
	}

	@Override
	public void drawRect(int x, int y, int width, int height, int r, int g, int b, int alpha) {
		// translate RGBA colors to MCs int color
		r = r & 0b00000000000000000000000011111111;
		g = g & 0b00000000000000000000000011111111;
		b = b & 0b00000000000000000000000011111111;
		alpha = alpha & 0b00000000000000000000000011111111;
		int color = (alpha << 24 | r << 16 | g << 8 | b);

		// draw the rectangle
		Gui.drawRect(x, y, x + width, y + height, color);
	}

	@Override
	public void drawString(String message, int x, int y, int r, int g, int b, int alpha) {
		// translate RGBA colors to MCs int color
		r = r & 0b00000000000000000000000011111111;
		g = g & 0b00000000000000000000000011111111;
		b = b & 0b00000000000000000000000011111111;
		alpha = alpha & 0b00000000000000000000000011111111;
		int color = (alpha << 24 | r << 16 | g << 8 | b);

		// draw the String
		Minecraft.getMinecraft().fontRenderer.drawString(message, x, y, color);

	}

	@Override
	public int stringWidth(String message) {
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(message);
	}

	@Override
	public int stringHeight() {
		return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
	}

	@Override
	public String gameVersion() {
		return Main.GAMEVERSION;
	}

	@Override
	public String wrapperVersion() {
		return Main.VERSION;
	}

}
