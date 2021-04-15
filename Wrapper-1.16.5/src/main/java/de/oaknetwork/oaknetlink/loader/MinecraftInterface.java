package de.oaknetwork.oaknetlink.loader;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.oaknetwork.oaknetlink.api.mcinterface.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;

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
		return Minecraft.getInstance().screen.height;
	}

	@Override
	public int screenWidth() {
		return Minecraft.getInstance().screen.width;
	}

	@Override
	public void connectToServer(String adress, int port) {
		//FMLIRGENDWAS.instance().connectToServer(new GuiMultiplayer(new GuiMainMenu()),
		//		new ServerData("OakNet-Link Server", adress + ":" + port, false));
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
		AbstractGui.fill(new MatrixStack(), x, y, x + width, y + height, color);
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
		Minecraft.getInstance().font.drawShadow(new MatrixStack(), message, x, y, color);

	}

	@Override
	public int stringWidth(String message) {
		return Minecraft.getInstance().font.width(message);
	}

	@Override
	public int stringHeight() {
		return Minecraft.getInstance().font.lineHeight;
	}

	@Override
	public String gameVersion() {
		return Main.GAMEVERSION;
	}

	@Override
	public String wrapperVersion() {
		return Main.VERSION;
	}

	@Override
	public String userName() {
		 return Minecraft.getInstance().getUser().getName();
	}

	@Override
	public String uuid() {
		return Minecraft.getInstance().getUser().getUuid();
	}

}
