package de.oaknetwork.oaknetlink.api.log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class provides a Logger for the Minecraft Server Log
 * 
 * @author Fabian Fila
 */
public class MinecraftServerLogProvider implements ILogProvider {

	@Override
	public void logInfo(String message, Class<?> sender) {
		//LogWindow.getInstance().getMcServerLogArea().append("[" + sender.getSimpleName() + "]: " + message + "\n");
		try {
			//LogWindow.getInstance().getMcServerLogArea()
			//		.setCaretPosition(LogWindow.getInstance().getMcServerLogArea().getText().length());
		} catch (Exception e) {
		}
	}

	@Override
	public void logWarning(String message, Class<?> sender) {
		//LogWindow.getInstance().getMcServerLogArea().append("[" + sender.getSimpleName() + "]: " + message + "\n");
		try {
			//LogWindow.getInstance().getMcServerLogArea()
					//.setCaretPosition(LogWindow.getInstance().getMcServerLogArea().getText().length());
		} catch (Exception e) {
		}
	}

	@Override
	public void logError(String message, Class<?> sender) {
		//LogWindow.getInstance().getMcServerLogArea().append("[" + sender.getSimpleName() + "]: " + message + "\n");
		try {
			//LogWindow.getInstance().getMcServerLogArea()
					//.setCaretPosition(LogWindow.getInstance().getMcServerLogArea().getText().length());
		} catch (Exception e) {
		}
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
