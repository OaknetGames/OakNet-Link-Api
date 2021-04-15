package de.oaknetwork.oaknetlink.api.log;

/**
 * This class should be implemented by LogProviders The LogProviders need to be
 * added to the Logger at init
 * 
 * @author Fabian Fila
 */
public interface ILogProvider {

	public void logInfo(String message, Class<?> sender);

	public void logWarning(String message, Class<?> sender);

	public void logError(String message, Class<?> sender);

	public void logException(String description, Exception except, Class<?> sender);
}
