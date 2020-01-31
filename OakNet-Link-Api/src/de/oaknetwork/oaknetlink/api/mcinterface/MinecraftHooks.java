package de.oaknetwork.oaknetlink.api.mcinterface;


//These methods have to be called from the wrapper
public class MinecraftHooks {

	public static IMinecraft mcInterface;
	
	public static void preInit(IMinecraft mcInterfaceIn) {
		mcInterface = mcInterfaceIn;
		mcInterface.logInfo("Hallo Welt!");
	}
	
	public static void init() {
	}
	
	public static void postInit() {
	}
}
