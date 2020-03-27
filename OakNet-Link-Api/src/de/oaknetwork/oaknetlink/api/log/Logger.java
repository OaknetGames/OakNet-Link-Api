package de.oaknetwork.oaknetlink.api.log;

import java.util.ArrayList;
import java.util.Optional;

import de.oaknetwork.oaknetlink.api.mcinterface.IMinecraft;

// this class will be used to log to the various levels
// there are shortcuts for logging to a default LogProvider
public class Logger {
	
	
	static ILogProvider standardLogProvider;
	
	static ArrayList<ILogProvider> logProvider = new ArrayList<ILogProvider>();
	
	public static void addLogProvider(ILogProvider logProviderToAdd) {
		logProvider.add(logProviderToAdd);
	}
	
	public static void setStandardLogProvider(ILogProvider logProvider) {
		standardLogProvider=logProvider;
	}
	
	public static void logInfo(String message, Class sender) {
		standardLogProvider.logInfo(message, sender);
	}
	
	public static void logWarning(String message, Class sender) {
		standardLogProvider.logWarning(message, sender);
	}
	
	public static void logError(String message, Class sender) {
		standardLogProvider.logError(message, sender);
	}
	
	public static void logException(String description, Exception except, Class sender) {
		standardLogProvider.logException(description, except, sender);
	}
	
	public static ILogProvider logProvider(Class clazz){
		Optional result = logProvider.stream().filter(element -> clazz.isInstance(element)).findFirst();
		return result.isPresent()?(ILogProvider)result.get():null;
	}

}
