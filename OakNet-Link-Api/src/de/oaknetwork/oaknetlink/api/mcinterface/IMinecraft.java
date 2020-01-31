package de.oaknetwork.oaknetlink.api.mcinterface;


// This Interface has to be implemented by the McWrapper
public interface IMinecraft {

	public void logInfo(String info);
	
	public void logWarning(String warning);
	
	public void logError(String error);
	
}
