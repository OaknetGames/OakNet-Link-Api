package de.oaknetwork.oaknetlink.api.gui.structure;

import de.oaknetwork.oaknetlink.api.gui.components.ListEntry;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * The ListEntry used for the ServerSelection Window
 * 
 * @author Fabian Fila
 */
public class ServerSelectionEntry extends ListEntry{

	public ServerSelectionEntry() {
		super(30);
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void initComponent() {		
	}

}
