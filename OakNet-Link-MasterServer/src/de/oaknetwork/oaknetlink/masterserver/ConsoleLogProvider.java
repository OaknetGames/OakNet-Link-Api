package de.oaknetwork.oaknetlink.masterserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.oaknetwork.oaknetlink.api.log.ILogProvider;

/**
 * This LogProvider logs to the Master-Server Console Window
 * 
 * @author Fabian
 */
public class ConsoleLogProvider implements ILogProvider{

	@Override
	public void logInfo(String message, Class sender) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();	
		String msg = String.format("[%S] [INFO] [" + sender.getSimpleName() + "]: %s", dtf.format(now), message);	
		System.out.println(msg);
	}

	@Override
	public void logWarning(String message, Class sender) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		String msg = String.format("[%S] [WARNING] [" + sender.getSimpleName() + "]: %s", dtf.format(now), message);	
		System.out.println(msg);		
		
		
	}

	@Override
	public void logError(String message, Class sender) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();	
		String msg = String.format("[%S] [ERROR] [" + sender.getSimpleName() + "]: %s", dtf.format(now), message);	
		System.out.println(msg);	
		
	}

	@Override
	public void logException(String description, Exception except, Class sender) {
		logError(description + ": " + except.getMessage(), sender);
		logError(except.getStackTrace().toString(), sender);
	}

}
