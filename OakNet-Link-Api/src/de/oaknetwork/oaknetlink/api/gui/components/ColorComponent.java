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

	private Color backgroundColor = Color.BLACK;
	private Color backgroundColorHighlight = Color.WHITE;
	private Color foregroundColor = Color.WHITE;
	private Color foregroundColorHighlight = Color.BLACK;
	private Color outlineColor = Color.WHITE;
	private Color outlineColorHighlight = Color.WHITE;

	public boolean useHighlights = true;
	public boolean inheritColorsFromParent = true;

	protected Color renderBackgroundColor = backgroundColor;
	protected Color renderForegroundColor = foregroundColor;
	protected Color renderOutlineColor = outlineColor;

	protected void setRenderColor(boolean overThisComponent) {
		renderBackgroundColor = overThisComponent ? backgroundColorHighlight : backgroundColor;
		renderForegroundColor = overThisComponent ? foregroundColorHighlight : foregroundColor;
		renderOutlineColor = overThisComponent ? outlineColorHighlight : outlineColor;
	}

	public void copyColors(ColorComponent component) {
		backgroundColor = component.backgroundColor;
		backgroundColorHighlight = component.backgroundColorHighlight;
		foregroundColor = component.foregroundColor;
		foregroundColorHighlight = component.foregroundColorHighlight;
		outlineColor = component.outlineColor;
		outlineColorHighlight = component.outlineColorHighlight;
	}

	private void notifyChildren() {
		for (Component child : children) {
			try {
				if (((ColorComponent) child).inheritColorsFromParent) {
					((ColorComponent) child).copyColors(this);
					((ColorComponent) child).setRenderColor(false);
				}
			} catch (ClassCastException e) {
				continue;
			}
		}
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		notifyChildren();
		setRenderColor(false);
	}

	public void setBackgroundColorHighlight(Color backgroundColorHighlight) {
		this.backgroundColorHighlight = backgroundColorHighlight;
		notifyChildren();
		setRenderColor(false);
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
		notifyChildren();
		setRenderColor(false);
	}

	public void setForegroundColorHighlight(Color foregroundColorHighlight) {
		this.foregroundColorHighlight = foregroundColorHighlight;
		notifyChildren();
		setRenderColor(false);
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		notifyChildren();
		setRenderColor(false);
	}

	public void setOutlineColorHighlight(Color outlineColorHighlight) {
		this.outlineColorHighlight = outlineColorHighlight;
		notifyChildren();
		setRenderColor(false);
	}

	@Override
	public boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta) {
		setRenderColor(mouseOverThisComponent(mousePos) && useHighlights);
		return false;
	}
}
