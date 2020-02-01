package de.oaknetwork.oaknetlink.api.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.utils.Constants;

// This LogProvider provides the log system for the api
public class OakNetLinkLogProvider implements ILogProvider {

	static String logFileName="";
	
	@Override
	public void logInfo(String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();		
		String msg = String.format("[%S] [INFO]: %s\n", dtf.format(now), message);
		LogWindow.getInstance().getOakNetLinkLogArea().append(msg);
		writeToLogFile(msg);
		}

	@Override
	public void logWarning(String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		String msg = String.format("[%S] [WARNING]: %s\n", dtf.format(now), message);
		LogWindow.getInstance().getOakNetLinkLogArea().append(msg);
		writeToLogFile(msg);
	}

	@Override
	public void logError(String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		String msg = String.format("[%S] [ERROR]: %s\n", dtf.format(now), message);
		LogWindow.getInstance().getOakNetLinkLogArea().append(msg);
		writeToLogFile(msg);
	}
	
	void writeToLogFile(String msg) {
		if(logFileName=="") {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY_MM_dd-HH_mm_ss");  
			LocalDateTime now = LocalDateTime.now();	
			logFileName=String.format(Constants.LOGPATH.toString()+"/Oaknet-Link-%s.log", dtf.format(now));
		}
		
		try {
			if(!Constants.LOGPATH.toFile().exists())
				Constants.LOGPATH.toFile().mkdirs();
		}catch(Exception e) {
			Logger.getInstance().getLogProvider(MinecraftLogProvider.class).logError("Can't create log dir: " + e.getMessage());
		}
		
		try(FileWriter fw = new FileWriter(logFileName, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(msg);
			} catch (IOException e) {
				Logger.getInstance().getLogProvider(MinecraftLogProvider.class).logError("Can't create log file: " + e.getMessage());
			}
	}

}
