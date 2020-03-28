package de.oaknetwork.oaknetlink.api.gui.structure;

import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.Color;
import de.oaknetwork.oaknetlink.api.gui.components.ColorComponent;
import de.oaknetwork.oaknetlink.api.gui.components.Component;
import de.oaknetwork.oaknetlink.api.gui.components.Label;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Constants;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * This class defines the structure of the Overlay
 * 
 * @author Fabian Fila
 */
public class Overlay extends ColorComponent {

	private Overlay instance;
	private CreateServerWindow createServerWindow;
	private ChatWindow chatWindow;
	private ServerListWindow serverListWindow;

	public Overlay(Component parent, Vector2i position, Vector2i size) {
		super(parent, position, size);
		createServerWindow = new CreateServerWindow(this);
		createServerWindow.close();
		chatWindow = new ChatWindow(this);
		chatWindow.close();
		serverListWindow = new ServerListWindow(this);
		serverListWindow.close();
		initComponent();
		useHighlights = false;
		instance = this;
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		return mouseOverThisComponent(clickPos);
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return mouseOverThisComponent(clickPos);
	}

	@Override
	public void renderComponent(Vector2i position) {
		GuiPrimitives.drawRect(position, size, renderBackgroundColor.copy().setAlpha(128));
		GuiPrimitives.drawRect(position, new Vector2i(size.X, 40), Color.DARKGRAY);
		GuiPrimitives.drawRect(position.copy().add(new Vector2i(0, 40)), new Vector2i(size.X, 2), Color.BLACK);
		GuiPrimitives.drawRect(position.copy().add(new Vector2i(0, size.Y - 20)), new Vector2i(size.X, 20),
				Color.DARKGRAY);
		GuiPrimitives.drawRect(position.copy().add(new Vector2i(0, size.Y - 22)), new Vector2i(size.X, 2), Color.BLACK);
	}

	@Override
	public void initComponent() {
		Label headerLabel = new Label(this, new Vector2i(0, 0), new Vector2i(size.X, 15), "OakNet-Link");
		headerLabel.useHighlights = false;
		headerLabel.setForegroundColor(Color.WHITE);

		Label versionLabel = new Label(this, new Vector2i(0, size.Y - 20), new Vector2i(size.X, 20),
				"OakNet-Link API: " + Constants.APIVERSION + "      " + "Wrapper: "
						+ MinecraftHooks.mcInterface.wrapperVersion() + " for "
						+ MinecraftHooks.mcInterface.gameVersion());
		versionLabel.useHighlights = false;
		versionLabel.setForegroundColor(Color.WHITE);

		Button closeButton = new Button(this, new Vector2i(size().X - 102, 15), new Vector2i(100, 20), "Close", 1,
				new Runnable() {

					@Override
					public void run() {
						parent.removeChild(instance);
					}
				});
		closeButton.setBackgroundColorHighlight(Color.LIGHTGRAY);
		closeButton.setForegroundColorHighlight(Color.BLACK);
		closeButton.setBackgroundColor(Color.DARKGRAY);
		closeButton.setOutlineColor(Color.BLACK);
		closeButton.setOutlineColorHighlight(Color.BLACK);

		Button createServerButton = new Button(this, new Vector2i(2, 15), new Vector2i(100, 20), "Create Server", 1,
				new Runnable() {

					@Override
					public void run() {
						createServerWindow.componentPosition = size.copy().divide(2)
								.add(createServerWindow.size().divide(2).negate());
						addChild(createServerWindow);
					}
				});
		createServerButton.setBackgroundColorHighlight(Color.LIGHTGRAY);
		createServerButton.setForegroundColorHighlight(Color.BLACK);
		createServerButton.setBackgroundColor(Color.DARKGRAY);
		createServerButton.setOutlineColor(Color.BLACK);
		createServerButton.setOutlineColorHighlight(Color.BLACK);

		Button serverListButton = new Button(this, new Vector2i(107, 15), new Vector2i(100, 20), "Server List", 1,
				new Runnable() {

					@Override
					public void run() {
						serverListWindow.componentPosition = size.copy().divide(2)
								.add(serverListWindow.size().divide(2).negate());
						addChild(serverListWindow);
					}
				});
		serverListButton.setBackgroundColorHighlight(Color.LIGHTGRAY);
		serverListButton.setForegroundColorHighlight(Color.BLACK);
		serverListButton.setBackgroundColor(Color.DARKGRAY);
		serverListButton.setOutlineColor(Color.BLACK);
		serverListButton.setOutlineColorHighlight(Color.BLACK);

		Button chatButton = new Button(this, new Vector2i(212, 15), new Vector2i(100, 20), "Chat", 1, new Runnable() {

			@Override
			public void run() {
				chatWindow.componentPosition = size.copy().divide(2).add(chatWindow.size().divide(2).negate());
				addChild(chatWindow);
			}
		});
		chatButton.setBackgroundColorHighlight(Color.LIGHTGRAY);
		chatButton.setForegroundColorHighlight(Color.BLACK);
		chatButton.setBackgroundColor(Color.DARKGRAY);
		chatButton.setOutlineColor(Color.BLACK);
		chatButton.setOutlineColorHighlight(Color.BLACK);

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

}
