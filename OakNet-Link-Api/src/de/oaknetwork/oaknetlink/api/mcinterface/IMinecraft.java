package de.oaknetwork.oaknetlink.api.mcinterface;


// This Interface has to be implemented by the McWrapper
public interface IMinecraft {

	public void logInfo(String info);
	
	public void logWarning(String warning);
	
	public void logError(String error);
	
	public int screenWidth();
	
	public int screenHeight();
	
	public String gameVersion();
	
	public void drawRect(int x, int y, int width, int height, int r, int g, int b, int alpha);
	
	public void drawString(String message, int x, int y, int r, int g, int b, int alpha);
	
	public int stringWidth(String message);
	
	public int stringHeight();
	
	public void connectToServer(String serverAdress, int port);
	
}
