package de.oaknetwork.oaknetlink.api.gui;

import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.Color;
import de.oaknetwork.oaknetlink.api.gui.components.Component;
import de.oaknetwork.oaknetlink.api.gui.components.ComponentContainer;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.gui.structure.Overlay;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * The GuiManager is a ComponentContainer with the size of the GameWindow and
 * acts as entrypoint for the OakNet-Link GUI System.
 * 
 * Every time the GameWindow size changes, the GuiManager will be resized.
 * 
 * The GuiManager is created in the PostInit phase
 * 
 * @author Fabian Fila
 */
public class GuiManager extends ComponentContainer {

	private static Vector2i currentSize = new Vector2i(0, 0);
	private static GuiManager instance;
	private static Overlay overlay;

	/**
	 * The GuiManager is created in the PostInit phase
	 */
	public GuiManager() {
		super(null, new Vector2i(0, 0), new Vector2i(0, 0));
		instance = this;
		overlay = new Overlay(this, new Vector2i(0, 0), new Vector2i(0, 0));
		removeChild(overlay);
	}

	@Override
	public void render(Vector2i position) {
		if (currentSize.X != MinecraftHooks.mcInterface.screenWidth()
				|| currentSize.Y != MinecraftHooks.mcInterface.screenHeight()) {
			resize(new Vector2i(MinecraftHooks.mcInterface.screenWidth(), MinecraftHooks.mcInterface.screenHeight()));
			currentSize = new Vector2i(MinecraftHooks.mcInterface.screenWidth(),
					MinecraftHooks.mcInterface.screenHeight());
			overlay.resize(currentSize);
		}
		super.render(position);
	}

	@Override
	public void initComponent() {
		Button button = new Button(instance(), new Vector2i(size().X - 102, 2), new Vector2i(100, 20), "OakNet-Link", 1,
				new Runnable() {

					@Override
					public void run() {
						addChild(overlay);
					}
				});
		button.setBackgroundColor(Color.DARKGRAY);
		button.setBackgroundColorHighlight(Color.LIGHTGRAY);
		button.setForegroundColorHighlight(Color.BLACK);
		button.setOutlineColor(Color.BLACK);
		button.setOutlineColorHighlight(Color.BLACK);
	}

	@Override
	public void resize(Vector2i newSize) {
		// we have to keep the opened Windows
		ArrayList<Window> windows = new ArrayList<Window>();
		for (Component child : children) {
			try {
				((Window) child).componentPosition = newSize.copy().divide(2)
						.add(child.size().copy().divide(2).negate());
				windows.add((Window) child);
			} catch (ClassCastException e) {
				continue;
			}
		}
		super.resize(newSize);
		for (Window window : windows) {
			addChild(window);
		}
	}

	/**
	 * Returns the current instance of the GuiManager
	 * 
	 * @return the instance
	 */
	public static GuiManager instance() {
		return instance;
	}
}
