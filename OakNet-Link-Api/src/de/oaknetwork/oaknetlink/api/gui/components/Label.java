package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class Label extends ColorComponent {
	
	public String text = "";
	public boolean centered=true;

	public Label(Component parent, Vector2i position, Vector2i size, String  text) {
		super(parent, position, size);
		this.text=text;
		initComponent();
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void renderComponent(Vector2i position) {
		if(centered) {
			Vector2i textPos = position.copy().add(size().divide(2)).add(new Vector2i(0, -(MinecraftHooks.mcInterface.stringHeight()/2)));
			GuiPrimitives.drawCenteredString(text, textPos, renderForegroundColor);
		}
		else
		{
			Vector2i textPos = position.copy().add(new Vector2i(0, size.Y/2-(MinecraftHooks.mcInterface.stringHeight()/2)));
			GuiPrimitives.drawString(text, textPos, renderForegroundColor);
		}
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void initComponent() {
	}

}
