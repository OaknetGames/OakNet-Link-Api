package de.oaknetwork.oaknetlink.api.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.oaknetwork.oaknetlink.api.utils.Constants;

/**
 * This LogProvider provides the log system for the api
 * 
 * @author Fabian Fila
 */
public class OakNetLinkLogProvider implements ILogProvider {

	static String logFileName = "";

	@Override
	public void logInfo(String message, Class<?> sender) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String msg = String.format("[%S] [INFO] [" + sender.getSimpleName() + "]: %s\n", dtf.format(now), message);
		//LogWindow.getInstance().getOakNetLinkLogArea().append(msg);
		try {
			//LogWindow.getInstance().getOakNetLinkLogArea()
					//.setCaretPosition(LogWindow.getInstance().getOakNetLinkLogArea().getText().length());
		} catch (Exception e) {
		}
		writeToLogFile(msg);
	}

	@Override
	public void logWarning(String message, Class<?> sender) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String msg = String.format("[%S] [WARNING] [" + sender.getSimpleName() + "]: %s\n", dtf.format(now), message);
		//LogWindow.getInstance().getOakNetLinkLogArea().append(msg);
		try {
			//LogWindow.getInstance().getOakNetLinkLogArea()
					//.setCaretPosition(LogWindow.getInstance().getOakNetLinkLogArea().getText().length());
		} catch (Exception e) {
		}
		writeToLogFile(msg);
	}

	@Override
	public void logError(String message, Class<?> sender) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String msg = String.format("[%S] [ERROR] [" + sender.getSimpleName() + "]: %s\n", dtf.format(now), message);
		//LogWindow.getInstance().getOakNetLinkLogArea().append(msg);
		try {
		//LogWindow.getInstance().getOakNetLinkLogArea()
				//.setCaretPosition(LogWindow.getInstance().getOakNetLinkLogArea().getText().length());
		} catch (Exception e) {
		}
		writeToLogFile(msg);
	}

	@Override
	public void logException(String description, Exception except, Class<?> sender) {
		logError(description + ": " + except.getMessage(), sender);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		except.printStackTrace(pw);
		logError(sw.toString().toString(), sender);
	}

	void writeToLogFile(String msg) {
		if (logFileName == "") {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY_MM_dd-HH_mm_ss");
			LocalDateTime now = LocalDateTime.now();
			logFileName = String.format(Constants.LOGPATH.toString() + "/Oaknet-Link-%s.log", dtf.format(now));
		}

		try {
			if (!Constants.LOGPATH.toFile().exists())
				Constants.LOGPATH.toFile().mkdirs();
		} catch (Exception e) {
			Logger.logProvider(MinecraftLogProvider.class).logError("Can't create log dir: " + e.getMessage(),
					OakNetLinkLogProvider.class);
		}

		try (FileWriter fw = new FileWriter(logFileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(msg);
		} catch (IOException e) {
			Logger.logProvider(MinecraftLogProvider.class).logError("Can't create log file: " + e.getMessage(),
					OakNetLinkLogProvider.class);
		}
	}

}
