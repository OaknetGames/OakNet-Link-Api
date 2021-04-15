package de.oaknetwork.oaknetlink.loader;


import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * In this EventHandler are some more events which are routed to the API
 * 
 * @author Fabian Fila
 */
public class OakNetEventHandler {

	@SubscribeEvent
	public void onRender(GuiScreenEvent.DrawScreenEvent.Post event) {
		MinecraftHooks.onRender();
	}

	@SubscribeEvent
	public void onMouse(GuiScreenEvent.MouseClickedEvent.Pre event) {
		int x = (int) event.getMouseX();
		int y = (int) event.getMouseY();
		int mouseButton = event.getButton();
			event.setCanceled(MinecraftHooks.mouseDown(x, y, mouseButton));
	}
	
	@SubscribeEvent
	public void onMouse(GuiScreenEvent.MouseReleasedEvent.Pre event) {
		int x = (int) event.getMouseX();
		int y = (int) event.getMouseY();
		int mouseButton = event.getButton();
		event.setCanceled(MinecraftHooks.mouseReleased(x, y, mouseButton));
	}
	
	@SubscribeEvent
	public void onMouse(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent  event) {
		int x = (int) event.getMouseX();
		int y = (int) event.getMouseY();
		int mouseWheelDelta = 0;
		MinecraftHooks.mouseOver(x, y, mouseWheelDelta);
	}
	
	@SubscribeEvent
	public void onMouse(GuiScreenEvent.MouseScrollEvent.Pre  event) {
		int x = (int) event.getMouseX();
		int y = (int) event.getMouseY();
		int mouseWheelDelta = (int) event.getScrollDelta();
		event.setCanceled(MinecraftHooks.mouseOver(x, y, mouseWheelDelta));
	}
	

	@SubscribeEvent
	public void onKeyboard(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
		event.setCanceled(MinecraftHooks.keyPressed('c', event.getKeyCode()));
	}
}
