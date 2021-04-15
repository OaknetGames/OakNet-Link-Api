package de.oaknetwork.oaknetlink.api.gui;

import de.oaknetwork.oaknetlink.api.gui.components.Color;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * With the help of this class primitive figures can be drawn during the draw
 * event
 * 
 * This is commonly used in GuiComponents in the renderComponent() method.
 * 
 * @author Fabian Fila
 */
public class GuiPrimitives {

	/**
	 * Draws a filled rectangle at pos with a specified size in the specified color.
	 * 
	 * @param pos
	 * @param size
	 * @param color
	 */
	public static void drawRect(Vector2i pos, Vector2i size, Color color) {
		if (MinecraftHooks.mcInterface == null)
			return;
		MinecraftHooks.mcInterface.drawRect(pos.X, pos.Y, size.X, size.Y, color.R, color.G, color.B, color.Alpha);
	}

	/**
	 * Draws a outlined rectangle at pos with a specified size in the specified
	 * color.
	 * 
	 * It also has an outlineSize thick outline which will be drawn in outlineColor.
	 * 
	 * @param pos
	 * @param size
	 * @param outlineSize
	 * @param color
	 * @param outlineColor
	 */
	public static void drawOutlinedRect(Vector2i pos, Vector2i size, int outlineSize, Color color, Color outlineColor) {
		if (MinecraftHooks.mcInterface == null)
			return;
		MinecraftHooks.mcInterface.drawRect(pos.X, pos.Y, size.X, outlineSize, outlineColor.R, outlineColor.G,
				outlineColor.B, outlineColor.Alpha);
		MinecraftHooks.mcInterface.drawRect(pos.X, pos.Y, outlineSize, size.Y, outlineColor.R, outlineColor.G,
				outlineColor.B, outlineColor.Alpha);
		MinecraftHooks.mcInterface.drawRect(pos.X + size.X - outlineSize, pos.Y, outlineSize, size.Y, outlineColor.R,
				outlineColor.G, outlineColor.B, outlineColor.Alpha);
		MinecraftHooks.mcInterface.drawRect(pos.X, pos.Y + size.Y - outlineSize, size.X, outlineSize, outlineColor.R,
				outlineColor.G, outlineColor.B, outlineColor.Alpha);
		MinecraftHooks.mcInterface.drawRect(pos.X + outlineSize, pos.Y + outlineSize, size.X - 2 * outlineSize,
				size.Y - 2 * outlineSize, color.R, color.G, color.B, color.Alpha);
	}

	/**
	 * Draws a left-aligned message at pos in color
	 * 
	 * @param message
	 * @param pos
	 * @param color
	 */
	public static void drawString(String message, Vector2i pos, Color color) {
		if (MinecraftHooks.mcInterface == null)
			return;
		MinecraftHooks.mcInterface.drawString(message, pos.X, pos.Y, color.R, color.G, color.B, color.Alpha);
	}

	/**
	 * Draws a centered message at pos in color
	 * 
	 * @param message
	 * @param pos
	 * @param color
	 */
	public static void drawCenteredString(String message, Vector2i pos, Color color) {
		if (MinecraftHooks.mcInterface == null)
			return;
		MinecraftHooks.mcInterface.drawString(message, pos.X - MinecraftHooks.mcInterface.stringWidth(message) / 2,
				pos.Y, color.R, color.G, color.B, color.Alpha);
	}

	/**
	 * Draws a right-aligned message at pos in color
	 * 
	 * @param message
	 * @param pos
	 * @param color
	 */
	public static void drawRightAllignedString(String message, Vector2i pos, Color color) {
		if (MinecraftHooks.mcInterface == null)
			return;
		MinecraftHooks.mcInterface.drawString(message, pos.X - MinecraftHooks.mcInterface.stringWidth(message), pos.Y,
				color.R, color.G, color.B, color.Alpha);
	}

}
