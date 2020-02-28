package de.oaknetwork.oaknetlink.api.gui;

import de.oaknetwork.oaknetlink.api.gui.components.Color;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class GuiPrimitives {
	
	public static void drawRect(Vector2i pos, Vector2i size, Color color) {
		if(MinecraftHooks.mcInterface==null)
			return;
		MinecraftHooks.mcInterface.drawRect(pos.X, pos.Y, size.X, size.Y, color.R, color.G, color.B, color.Alpha);
	}
	
	public static void drawOutlinedRect(Vector2i pos, Vector2i size, int outlineSize, Color color, Color outlineColor) {
		if(MinecraftHooks.mcInterface==null)
			return;
		MinecraftHooks.mcInterface.drawRect(pos.X, pos.Y, size.X, outlineSize, outlineColor.R, outlineColor.G, outlineColor.B, outlineColor.Alpha);
		MinecraftHooks.mcInterface.drawRect(pos.X, pos.Y, outlineSize, size.Y, outlineColor.R, outlineColor.G, outlineColor.B, outlineColor.Alpha);
		MinecraftHooks.mcInterface.drawRect(pos.X + size.X - outlineSize, pos.Y, outlineSize, size.Y, outlineColor.R, outlineColor.G, outlineColor.B, outlineColor.Alpha);
		MinecraftHooks.mcInterface.drawRect(pos.X, pos.Y + size.Y - outlineSize, size.X, outlineSize, outlineColor.R, outlineColor.G, outlineColor.B, outlineColor.Alpha);
		MinecraftHooks.mcInterface.drawRect(pos.X + outlineSize, pos.Y + outlineSize, size.X - 2 * outlineSize, size.Y - 2 * outlineSize, color.R, color.G, color.B, color.Alpha);
	}
	
	public static void drawString(String message, Vector2i pos, Color color) {
		if(MinecraftHooks.mcInterface==null)
			return;
		MinecraftHooks.mcInterface.drawString(message, pos.X, pos.Y, color.R, color.G, color.B, color.Alpha);
	}
	
	public static void drawCenteredString(String message, Vector2i pos, Color color) {
		if(MinecraftHooks.mcInterface==null)
			return;
		MinecraftHooks.mcInterface.drawString(message, pos.X - MinecraftHooks.mcInterface.stringWidth(message) / 2, pos.Y, color.R, color.G, color.B, color.Alpha);
	}
	
	public static void drawRightAllignedString(String message, Vector2i pos, Color color) {
		if(MinecraftHooks.mcInterface==null)
			return;
		MinecraftHooks.mcInterface.drawString(message, pos.X - MinecraftHooks.mcInterface.stringWidth(message), pos.Y, color.R, color.G, color.B, color.Alpha);
	}

}
