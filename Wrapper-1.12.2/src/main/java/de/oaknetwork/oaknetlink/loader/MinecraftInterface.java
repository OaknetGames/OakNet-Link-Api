package de.oaknetwork.oaknetlink.loader;

import de.oaknetwork.oaknetlink.api.mcinterface.IMinecraft;
import net.minecraft.client.gui.Gui;

public class MinecraftInterface implements IMinecraft{

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
	public void drawRect(int x, int y, int width, int height, int color) {
		Gui.drawRect(x, y, width, height, color);
	}

}
