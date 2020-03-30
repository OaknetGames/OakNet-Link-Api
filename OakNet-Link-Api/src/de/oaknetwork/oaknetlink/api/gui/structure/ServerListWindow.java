package de.oaknetwork.oaknetlink.api.gui.structure;

import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.Color;
import de.oaknetwork.oaknetlink.api.gui.components.Component;
import de.oaknetwork.oaknetlink.api.gui.components.List;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * This class defines the structure of the ServerListWindow
 * 
 * @author Fabian Fila
 */
public class ServerListWindow extends Window{
	
	private static final int margin = 2;
	private static final int outlineSize = 1;
	
	public ServerListWindow(Component parent) {
		super(parent, new Vector2i(0, 0), new Vector2i(220, 250), 1, "Server List", true);
	}

	@Override
	public void initComponent() {
		super.initComponent();
		List<ServerSelectionEntry> serverSelectionList = new List<ServerSelectionEntry>(this, new Vector2i(margin, margin), contentSize().add(new Vector2i(-2*margin, -3*margin-20)), outlineSize);
		serverSelectionList.inheritColorsFromParent=false;
		serverSelectionList.setOutlineColor(Color.WHITE);
		new Button(this, contentSize().add(new Vector2i(-100-margin, -20-margin)), new Vector2i(100, 20), "Close", outlineSize, new Runnable() {
			
			@Override
			public void run() {
				close();
			}
		});
		new Button(this, new Vector2i(margin, contentSize().Y-margin-20), new Vector2i(100, 20), "Connect to Server", outlineSize, new Runnable() {
	
			@Override
			public void run() {
				close();
			}
		});
	}
}
