package de.oaknetwork.oaknetlink.api.log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

/**
 * This class steals the log from Minecraft's Log4J logger
 * 
 * @author Fabian Fila
 */
public class MinecraftLogAppender extends AbstractAppender {

	protected MinecraftLogAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout);
	}

	@Override
	public void append(LogEvent e) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		LogWindow.getInstance().getMcLogArea().append("[" + dtf.format(now) + "] [" + e.getLevel().name() + "] ["
				+ e.getLoggerName() + "]: " + e.getMessage().getFormattedMessage() + "\n");
		try {
			LogWindow.getInstance().getMcLogArea()
					.setCaretPosition(LogWindow.getInstance().getMcLogArea().getText().length());
		} catch (Exception except) {
		}
	}

}
