package de.oaknetwork.oaknetlink.api.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MinecraftServerLogProvider implements ILogProvider{

	@Override
	public void logInfo(String message, Class sender) {
		LogWindow.getInstance().getMcServerLogArea().append("[" + sender.getSimpleName() + "]: " + message);
		}

	@Override
	public void logWarning(String message, Class sender) {
		LogWindow.getInstance().getMcServerLogArea().append("[" + sender.getSimpleName() + "]: " + message);
	}

	@Override
	public void logError(String message, Class sender) {
		LogWindow.getInstance().getMcServerLogArea().append("[" + sender.getSimpleName() + "]: " + message);
	}
	
	@Override
	public void logException(String description, Exception except, Class sender) {
		logError(description + ": " + except.getMessage(), sender);
		logError(except.getStackTrace().toString(), sender);
	}

}
