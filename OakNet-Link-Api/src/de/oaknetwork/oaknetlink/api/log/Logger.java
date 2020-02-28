package de.oaknetwork.oaknetlink.api.log;

import java.util.ArrayList;
import java.util.Optional;

import de.oaknetwork.oaknetlink.api.mcinterface.IMinecraft;

// this class will be used to log to the various levels
// it should be a singleton to make sure we keep track a existing mcInterface
public class Logger {
	

	static Logger instance;
	
	ArrayList<ILogProvider> logProvider = new ArrayList<ILogProvider>();
	
	public Logger(IMinecraft mcInterface) {
		this.instance = this;
		
		// add the logProviders
		logProvider.add(new MinecraftLogProvider(mcInterface));
		logProvider.add(new OakNetLinkLogProvider());
		logProvider.add(new MinecraftServerLogProvider());
		
	}
	
	public static Logger instance(){
		return instance;
	}
	
	public ILogProvider logProvider(Class clazz){
		Optional result = logProvider.stream().filter(element -> clazz.isInstance(element)).findFirst();
		return result.isPresent()?(ILogProvider)result.get():null;
	}

}
