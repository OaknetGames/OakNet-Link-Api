package de.oaknetwork.oaknetlink.api.log;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.layout.PatternLayout;

import de.oaknetwork.oaknetlink.api.mcinterface.IMinecraft;

// This LogProvider provides a Logger for the default Minecraft Log
public class MinecraftLogProvider implements ILogProvider{

	IMinecraft mcInterface;
	
	ArrayList<String> mcLog = new ArrayList<String>();
	
	public MinecraftLogProvider(IMinecraft minecraftInterface) {
		mcInterface = minecraftInterface;

	    // Create and add the appender
	    LoggerContext lc = (LoggerContext) LogManager.getContext(false);
	    MinecraftLogAppender appender = new MinecraftLogAppender("OakNet-Link Log Appender", null, PatternLayout.createDefaultLayout());
	    appender.start();
	    lc.getConfiguration().addAppender(appender);
	    lc.getRootLogger().addAppender(lc.getConfiguration().getAppender(appender.getName()));
	    lc.updateLoggers();
	}

	@Override
	public void logInfo(String message) {
		mcInterface.logInfo(message);
	}

	@Override
	public void logWarning(String message) {
		mcInterface.logWarning(message);	
	}

	@Override
	public void logError(String message) {
		mcInterface.logError(message);
	}

}