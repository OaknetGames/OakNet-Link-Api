package de.oaknetwork.oaknetlink.api.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MinecraftServerLogProvider implements ILogProvider{

	@Override
	public void logInfo(String message) {
		LogWindow.getInstance().getMcServerLogArea().append(message);
		}

	@Override
	public void logWarning(String message) {
		LogWindow.getInstance().getMcServerLogArea().append(message);
	}

	@Override
	public void logError(String message) {
		LogWindow.getInstance().getMcServerLogArea().append(message);
	}

}
