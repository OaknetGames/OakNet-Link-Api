package de.oaknetwork.oaknetlink.api.log;

import java.util.ArrayList;

public interface ILogProvider {
		
	public void logInfo(String message);
	
	public void logWarning(String message);
	
	public void logError(String message);
}
