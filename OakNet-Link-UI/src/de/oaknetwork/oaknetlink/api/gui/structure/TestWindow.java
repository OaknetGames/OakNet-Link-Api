package de.oaknetwork.oaknetlink.api.gui.structure;

import de.oaknetwork.oaknetlink.api.gui.GuiManager;
import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class TestWindow extends Window {

	public TestWindow() {
		super(GuiManager.overlay, new Vector2i(0, 0), new Vector2i(408, 69), 1, "OakNet-Link Test Window", false);
	}

	@Override
	public void initComponent() {
		super.initComponent();
		new Button(componentContainer, new Vector2i(2, 2), new Vector2i(200, 20), "Create Test-Game", 1, new Runnable() {

			@Override
			public void run() {
			}
		});
		new Button(componentContainer, new Vector2i(204, 2), new Vector2i(200, 20), "Change State to Running", 1, new Runnable() {

			@Override
				public void run() {
			}
		});
		new Button(componentContainer, new Vector2i(2, 24), new Vector2i(200, 20), "Connect to MasterServer via UDP", 1, new Runnable() {
			@Override
			public void run() {
			}
		});
		new Button(componentContainer, new Vector2i(204, 24), new Vector2i(200, 20), "Connect to Test-Game", 1, new Runnable() {
			@Override
			public void run() {
			}
		});
	}

}
