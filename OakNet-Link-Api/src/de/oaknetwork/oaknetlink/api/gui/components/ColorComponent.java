package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public abstract class ColorComponent extends Component {

	public ColorComponent(Component parent, Vector2i position, Vector2i size) {
		super(parent, position, size);
		// Check if parent is a ColorComponent and copy its Colors
		if (parent != null) {
			if (parent instanceof ColorComponent) {
				ColorComponent parentComponent = (ColorComponent) parent;
				copyColors(parentComponent);
			}
		}
	}

	public Color backgroundColor = Color.BLACK;
	public Color backgroundColorHighlight = Color.WHITE;
	public Color foregroundColor = Color.WHITE;
	public Color foregroundColorHighlight = Color.BLACK;
	public Color outlineColor = Color.BLACK;
	public Color outlineColorHighlight = Color.BLACK;

	Color renderBackgroundColor = backgroundColor;
	Color renderForegroundColor = foregroundColor;
	Color renderOutlineColor = outlineColor;

	private void setRenderColor(boolean overThisComponent) {
		renderBackgroundColor = overThisComponent ? backgroundColorHighlight : backgroundColor;
		renderForegroundColor = overThisComponent ? foregroundColorHighlight : foregroundColor;
		renderOutlineColor = overThisComponent ? outlineColorHighlight : outlineColor;
	}

	public void copyColors(ColorComponent component) {
		backgroundColor = component.backgroundColor;
		backgroundColorHighlight = component.backgroundColorHighlight;
		foregroundColor = component.foregroundColor;
		foregroundColorHighlight = component.foregroundColorHighlight;
		outlineColor=component.outlineColor;
		outlineColorHighlight=component.outlineColorHighlight;
	}

	@Override
	public boolean mouseOverComponent(Vector2i mousePos) {
		setRenderColor(mouseOverThisComponent(mousePos));
		return false;
	}
}
