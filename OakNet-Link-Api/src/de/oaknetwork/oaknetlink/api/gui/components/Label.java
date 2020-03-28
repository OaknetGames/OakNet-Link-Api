package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * Just a simple Label to display some Text
 *
 * @author Fabian Fila
 */
public class Label extends ColorComponent {

	/**
	 * The text which will be displayed
	 */
	public String text = "";
	/**
	 * Should the text be centered in the label
	 */
	public boolean centered = true;

	/**
	 * Creates a new Label
	 * 
	 * @param parent   the parent of this Component
	 * @param position the position of this Component relative to its parent
	 * @param size     the size of the Component
	 * @param text     The text which will be displayed
	 */
	public Label(Component parent, Vector2i position, Vector2i size, String text) {
		super(parent, position, size);
		this.text = text;
		initComponent();
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void renderComponent(Vector2i position) {
		if (centered) {
			Vector2i textPos = position.copy().add(size().divide(2))
					.add(new Vector2i(0, -(MinecraftHooks.mcInterface.stringHeight() / 2)));
			GuiPrimitives.drawCenteredString(text, textPos, renderForegroundColor);
		} else {
			Vector2i textPos = position.copy()
					.add(new Vector2i(0, size.Y / 2 - (MinecraftHooks.mcInterface.stringHeight() / 2)));
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
