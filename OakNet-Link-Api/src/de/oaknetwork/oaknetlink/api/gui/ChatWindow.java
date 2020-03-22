package de.oaknetwork.oaknetlink.api.gui;

import de.oaknetwork.oaknetlink.api.gui.components.Component;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class ChatWindow extends Window{

	public ChatWindow(Component parent) {
		super(parent, new Vector2i(0, 0), new Vector2i(250, 200), 1, "Chat", true);
	}

}
