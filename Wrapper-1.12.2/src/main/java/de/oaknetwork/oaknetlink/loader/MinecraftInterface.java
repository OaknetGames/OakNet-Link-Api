package de.oaknetwork.oaknetlink.loader;

import de.oaknetwork.oaknetlink.api.mcinterface.IMinecraft;

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

}
