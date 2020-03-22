package de.oaknetwork.oaknetlink.loader;

import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.Logger;

import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.updater.Updater;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main
{
    public static final String MODID = "oaknetlink";
    public static final String NAME = "OakNet-Link 1.12.2";
    public static final String VERSION = "DEV-1.0";
    public static final String GAMEVERSION = "1.12.2";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	Updater.checkForUpdates(GAMEVERSION);
        logger = event.getModLog();
        MinecraftHooks.preInit(new MinecraftInterface());
        MinecraftForge.EVENT_BUS.register(new OakNetEventHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	 MinecraftHooks.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	 MinecraftHooks.postInit();
    }

    
    public static Logger getLogger() {
		return logger;
	}
}
