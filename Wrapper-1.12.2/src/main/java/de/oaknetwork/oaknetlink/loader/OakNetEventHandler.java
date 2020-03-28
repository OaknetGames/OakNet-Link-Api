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
	public void onMouse(GuiScreenEvent.MouseInputEvent.Pre event) {
		int x = (int) ((Mouse.getX() * event.getGui().width) / (double) Minecraft.getMinecraft().displayWidth);
		int y = (int) (event.getGui().height
				- (Mouse.getY() * event.getGui().height) / (double) Minecraft.getMinecraft().displayHeight);
		int mouseButton = Mouse.getEventButton();
		int mouseWheelDelta = Mouse.getDWheel();
		if (mouseButton == -1)
			event.setCanceled(MinecraftHooks.mouseOver(x, y, mouseWheelDelta));
		else if (Mouse.getEventButtonState())
			event.setCanceled(MinecraftHooks.mouseDown(x, y, mouseButton));
		else
			event.setCanceled(MinecraftHooks.mouseReleased(x, y, mouseButton));

	}

	@SubscribeEvent
	public void onKeyboard(GuiScreenEvent.KeyboardInputEvent.Pre event) {
		if (Keyboard.getEventKeyState())
			event.setCanceled(MinecraftHooks.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey()));
	}
}
