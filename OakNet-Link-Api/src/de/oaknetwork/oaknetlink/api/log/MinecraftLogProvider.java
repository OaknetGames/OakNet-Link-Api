package de.oaknetwork.oaknetlink.api.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.layout.PatternLayout;

import de.oaknetwork.oaknetlink.api.mcinterface.IMinecraft;

/**
 * This LogProvider provides a Logger for the default Minecraft Log
 * 
 * @author Fabian Fila
 */
public class MinecraftLogProvider implements ILogProvider {

	IMinecraft mcInterface;

	ArrayList<String> mcLog = new ArrayList<String>();

	public MinecraftLogProvider(IMinecraft minecraftInterface) {
		mcInterface = minecraftInterface;

		// Create and add the appender
		LoggerContext lc = (LoggerContext) LogManager.getContext(false);
		MinecraftLogAppender appender = new MinecraftLogAppender("OakNet-Link Log Appender", null,
				PatternLayout.createDefaultLayout());
		appender.start();
		lc.getConfiguration().addAppender(appender);
		lc.getRootLogger().addAppender(lc.getConfiguration().getAppender(appender.getName()));
		lc.updateLoggers();
	}

	@Override
	public void logInfo(String message, Class<?> sender) {
		mcInterface.logInfo("[" + sender.getSimpleName() + "]: " + message);
	}

	@Override
	public void logWarning(String message, Class<?> sender) {
		mcInterface.logWarning("[" + sender.getSimpleName() + "]: " + message);
	}

	@Override
	public void logError(String message, Class<?> sender) {
		mcInterface.logError("[" + sender.getSimpleName() + "]: " + message);
	}

	@Override
	public void logException(String description, Exception except, Class<?> sender) {
		logError(description + ": " + except.getMessage(), sender);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		except.printStackTrace(pw);
		logError(sw.toString().toString(), sender);
	}

}
