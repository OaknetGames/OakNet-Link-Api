package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * This class derives from the default Component and adds some color related
 * logic to it.
 * 
 * Like MouseOver highlighting, or the inheritance of parent colors to their
 * children.
 * 
 * It provides some renderColors which can be used for rendering the Component.
 * 
 * @author Fabian Fila
 */
public abstract class ColorComponent extends Component {

	/**
	 * Creates a new ColorComponent
	 * 
	 * @param parent   the parent of this ColorComponent
	 * @param position the position of this Component relative to its parent
	 * @param size     the size of the Component
	 */
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
	private Color backgroundColorHighlight = Color.DARKGRAY;
	private Color foregroundColor = Color.WHITE;
	private Color foregroundColorHighlight = Color.WHITE;
	private Color outlineColor = Color.WHITE;
	private Color outlineColorHighlight = Color.WHITE;

	/**
	 * Enable the highlighting
	 */
	public boolean useHighlights = true;

	/**
	 * Enable the inheritance behavior
	 */
	public boolean inheritColorsFromParent = true;

	protected Color renderBackgroundColor = backgroundColor;
	protected Color renderForegroundColor = foregroundColor;
	protected Color renderOutlineColor = outlineColor;

	protected void setRenderColor(boolean overThisComponent) {
		renderBackgroundColor = overThisComponent ? backgroundColorHighlight : backgroundColor;
		renderForegroundColor = overThisComponent ? foregroundColorHighlight : foregroundColor;
		renderOutlineColor = overThisComponent ? outlineColorHighlight : outlineColor;
	}

	/**
	 * Used for copying colors from another ColorComponent
	 * 
	 * @param component the component to copy the colors from
	 */
	public void copyColors(ColorComponent component) {
		backgroundColor = component.backgroundColor;
		backgroundColorHighlight = component.backgroundColorHighlight;
		foregroundColor = component.foregroundColor;
		foregroundColorHighlight = component.foregroundColorHighlight;
		outlineColor = component.outlineColor;
		outlineColorHighlight = component.outlineColorHighlight;
		setRenderColor(false);
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

	/**
	 * Sets the backGroundColor
	 * 
	 * @param backgroundColor the backGroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		notifyChildren();
		setRenderColor(false);
	}

	/**
	 * Sets the backgroundColorHighlight
	 * 
	 * @param backgroundColorHighlight the backgroundColorHighlight to set
	 */
	public void setBackgroundColorHighlight(Color backgroundColorHighlight) {
		this.backgroundColorHighlight = backgroundColorHighlight;
		notifyChildren();
		setRenderColor(false);
	}

	/**
	 * Sets the foregroundColor
	 * 
	 * @param foregroundColor the foregroundColor to set
	 */
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
		notifyChildren();
		setRenderColor(false);
	}

	/**
	 * Sets the foregroundColorHighlight
	 * 
	 * @param foregroundColorHighlight the foregroundColorHighlight to set
	 */
	public void setForegroundColorHighlight(Color foregroundColorHighlight) {
		this.foregroundColorHighlight = foregroundColorHighlight;
		notifyChildren();
		setRenderColor(false);
	}

	/**
	 * Sets the outlineColor
	 * 
	 * @param outlineColor the outlineColor to set
	 */
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		notifyChildren();
		setRenderColor(false);
	}

	/**
	 * Sets the outlineColorHighlight
	 * 
	 * @param outlineColorHighlight the outlineColorHighlight to set
	 */
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
