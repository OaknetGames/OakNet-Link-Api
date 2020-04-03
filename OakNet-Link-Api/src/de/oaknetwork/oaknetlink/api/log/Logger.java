package de.oaknetwork.oaknetlink.api.log;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This class will be used to log to the various levels. Also there are
 * shortcuts for logging to a default LogProvider.
 * 
 * @author Fabian Fila
 */
public class Logger {

	static ILogProvider defaultLogProvider;

	static ArrayList<ILogProvider> logProvider = new ArrayList<ILogProvider>();

	/**
	 * Register a new ILogProvider This should be done at init
	 * 
	 * @param logProviderToAdd the LogProvider to add
	 */
	public static void addLogProvider(ILogProvider logProviderToAdd) {
		logProvider.add(logProviderToAdd);
	}

	/**
	 * Use this to set a default LogProvider
	 * 
	 * @param logProvider the default LogProvider
	 */
	public static void setDefaultLogProvider(ILogProvider logProvider) {
		defaultLogProvider = logProvider;
	}

	/**
	 * Shortcut to log info to the defaultLogProvider
	 * 
	 * @param message the Message which will be logged
	 * @param sender  the class which logged
	 */
	public static void logInfo(String message, Class<?> sender) {
		if (defaultLogProvider == null)
			throw new RuntimeException("Default LogProvider not set");
		defaultLogProvider.logInfo(message, sender);
	}

	/**
	 * Shortcut to log warnings to the defaultLogProvider
	 * 
	 * @param message the Message which will be logged
	 * @param sender  the class which logged
	 */
	public static void logWarning(String message, Class<?> sender) {
		if (defaultLogProvider == null)
			throw new RuntimeException("Default LogProvider not set");
		defaultLogProvider.logWarning(message, sender);
	}

	/**
	 * Shortcut to log errors to the defaultLogProvider
	 * 
	 * @param message the Message which will be logged
	 * @param sender  the class which logged
	 */
	public static void logError(String message, Class<?> sender) {
		if (defaultLogProvider == null)
			throw new RuntimeException("Default LogProvider not set");
		defaultLogProvider.logError(message, sender);
	}

	/**
	 * Shortcut to log exceptions to the defaultLogProvider
	 * 
	 * @param message the Message which will be logged
	 * @param sender  the class which logged
	 */
	public static void logException(String description, Exception except, Class<?> sender) {
		if (defaultLogProvider == null)
			throw new RuntimeException("Default LogProvider not set");
		defaultLogProvider.logException(description, except, sender);
	}

	/**
	 * Log info to multiple LogProviders
	 * 
	 * @param message   the Message which will be logged
	 * @param sender    the class which logged
	 * @param providers the providers to which should be logged
	 */
	public static void logInfo(String message, Class<?> sender, Class<?>... providers) {
		for (Class<?> clazz : providers) {
			ILogProvider logProvider = logProvider(clazz);
			if (logProvider == null)
				throw new RuntimeException("Invalid LogProvider");
			logProvider.logInfo(message, sender);
		}
	}

	/**
	 * Log warning to multiple LogProviders
	 * 
	 * @param message   the Message which will be logged
	 * @param sender    the class which logged
	 * @param providers the providers to which should be logged
	 */
	public static void logWarning(String message, Class<?> sender, Class<?>... providers) {
		for (Class<?> clazz : providers) {
			ILogProvider logProvider = logProvider(clazz);
			if (logProvider == null)
				throw new RuntimeException("Invalid LogProvider");
			logProvider.logWarning(message, sender);
		}
	}

	/**
	 * Log errors to multiple LogProviders
	 * 
	 * @param message   the Message which will be logged
	 * @param sender    the class which logged
	 * @param providers the providers to which should be logged
	 */
	public static void logError(String message, Class<?> sender, Class<?>... providers) {
		for (Class<?> clazz : providers) {
			ILogProvider logProvider = logProvider(clazz);
			if (logProvider == null)
				throw new RuntimeException("Invalid LogProvider");
			logProvider.logError(message, sender);
		}
	}

	/**
	 * Log exceptions to multiple LogProviders
	 * 
	 * @param message   the Message which will be logged
	 * @param sender    the class which logged
	 * @param providers the providers to which should be logged
	 */
	public static void logException(String description, Exception except, Class<?> sender, Class<?>... providers) {
		for (Class<?> clazz : providers) {
			ILogProvider logProvider = logProvider(clazz);
			if (logProvider == null)
				throw new RuntimeException("Invalid LogProvider");
			logProvider.logException(description, except, sender);
		}
	}

	/**
	 * Used to get a registered LogProvider by its class
	 * 
	 * @param clazz the class of the LogProvider
	 * @return the registered instance of the LogProvider
	 */
	public static ILogProvider logProvider(Class<?> clazz) {
		Optional<ILogProvider> result = logProvider.stream().filter(element -> clazz.isInstance(element)).findFirst();
		return result.isPresent() ? (ILogProvider) result.get() : null;
	}

}
