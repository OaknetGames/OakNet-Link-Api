package de.oaknetwork.oaknetlink.loader;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.updater.Updater;

/**
 * This is the Main Class of the 1.12.2 Wrapper
 * 
 * It will be loaded by Minecraft Forge
 * 
 * @author Fabian Fila
 */
@Mod("oaknetlink")
public class Main {
	/*
	 * Some useful constants
	 */
	public static final String MODID = "oaknetlink";
	public static final String NAME = "OakNet-Link 1.16.6";
	public static final String VERSION = "DEV-1.0";
	public static final String GAMEVERSION = "1.16.5";

	private static Logger logger = LogManager.getLogger();

	public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	/**
	 * Subscribing to init events and route them to the API
	 */
	public void preInit(final FMLCommonSetupEvent event) {
		Updater.checkForUpdates(GAMEVERSION);
		MinecraftHooks.preInit(new MinecraftInterface());
		MinecraftForge.EVENT_BUS.register(new OakNetEventHandler());
	}

	public void init(FMLClientSetupEvent event) {
		MinecraftHooks.init();
	}

	public void postInit(FMLLoadCompleteEvent event) {
		MinecraftHooks.postInit();
	}

	/**
	 * The logger will be used in the MinecraftLogProvider
	 */
	public static Logger getLogger() {
		return logger;
	}
}
