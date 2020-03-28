package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * This class need to be overridden for the ListEntry implementation, the width
 * of the entry will be calculated from the parent list.
 * 
 * @author Fabian Fila
 */
public abstract class ListEntry extends ColorComponent {

	/**
	 * Is the entry selected
	 */
	public boolean selected = false;

	/**
	 * Create a new ListEntry with the given height
	 * 
	 * @param height
	 */
	public ListEntry(int height) {
		super(null, new Vector2i(0, 0), new Vector2i(0, height));
	}

	/**
	 * Sets a new parent component, this is used to hide entries which are not shown
	 * at the moment.
	 * 
	 * @param newParent
	 */
	public void setParent(Component newParent) {
		if (newParent == null)
			parent.removeChild(this);
		else {
			newParent.addChild(this);
		}
		parent = newParent;
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		if (!mouseOverThisComponent(clickPos))
			return false;
		try {
			List parentList = (List) parent;
			parentList.deselectAll();
			selected = true;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public void renderComponent(Vector2i position) {
		if (selected)
			GuiPrimitives.drawRect(position, size(), renderForegroundColor);
	}
}
