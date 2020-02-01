package de.oaknetwork.oaknetlink.loader;

import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OakNetEventHandler {

    
    @SubscribeEvent
    public void onRender(GuiScreenEvent.DrawScreenEvent event) {
    	MinecraftHooks.onRender();
    }
	
}
