package de.oaknetwork.oaknetlink.loader;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class OakNetEventHandler {

    
    @SubscribeEvent
    public void onRender(GuiScreenEvent.DrawScreenEvent.Post event) {
    	MinecraftHooks.onRender();
    }
    
    @SubscribeEvent
    public void onMouse(GuiScreenEvent.MouseInputEvent.Pre event) {
    	 int x = (Mouse.getX() * event.getGui().width) / Minecraft.getMinecraft().displayWidth; 
         int y = event.getGui().height - (Mouse.getY() * event.getGui().height) / Minecraft.getMinecraft().displayHeight;
    	 int mouseButton = Mouse.getEventButton();
    	 if(mouseButton == -1)
    		 event.setCanceled(MinecraftHooks.mouseOver(x, y));
    	 else if(Mouse.getEventButtonState())
    		 event.setCanceled(MinecraftHooks.click(x, y, mouseButton));
    }
    
    @SubscribeEvent
	public void onKeyboard(GuiScreenEvent.KeyboardInputEvent event) {
    	event.setCanceled(MinecraftHooks.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey()));
    }
}
