package de.oaknetwork.oaknetlink.api.gui.components;

import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * This Class provides a Component which contains a List of items, of which the
 * user can change one.
 * 
 * The List will be shown in a dropdown menu.
 * 
 * @author Fabian Fila
 */
public class ComboBox extends ColorComponent {

	private ArrayList<ComboBoxItem> items = new ArrayList<>();
	private int selectedIdx = 0;
	private Vector2i defaultSize;
	private int outlineSize = 1;
	private boolean expanded = false;
	private Label arrowLabel;
	private Label currentItemLabel;

	/**
	 * Creates a new Combobox
	 * 
	 * @param parent      the parent of this ColorComponent
	 * @param position    the position of this Component relative to its parent
	 * @param size        the size of the Component
	 * @param outlineSize the thickness of the outline
	 */
	public ComboBox(Component parent, Vector2i position, Vector2i size, int outlineSize) {
		super(parent, position, size);
		defaultSize = size.copy();
		this.outlineSize = outlineSize;
		initComponent();
	}

	/**
	 * Select an item with index
	 * 
	 * @param idx the index which should be selected
	 */
	public void selectIdx(int idx) {
		if (idx < items.size()) {
			selectedIdx = idx;
		}
	}

	/**
	 * Select a specific item
	 * 
	 * @param item the item to select
	 */
	public void selectItem(ComboBoxItem item) {
		selectedIdx = items.indexOf(item) != -1 ? items.indexOf(item) : selectedIdx;
	}

	/**
	 * Get the current selected item
	 * 
	 * Returns null if nothing is selected
	 * 
	 * @return the selected item
	 */
	public ComboBoxItem selectedItem() {
		return selectedIdx >= items.size() ? null : items.get(selectedIdx);
	}

	/**
	 * Add a new item to the ComboBox
	 * 
	 * @param item the item to add
	 */
	public void addItem(ComboBoxItem item) {
		items.add(item);
		children.clear();
		initComponent();
	}

	private void calcSize() {
		if (expanded) {
			Vector2i newSize = defaultSize.copy();
			newSize.add(new Vector2i(0, 20 * items.size()));
			resize(newSize);
		} else
			resize(defaultSize);
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		if (mouseOverThisComponent(clickPos)) {
			expanded = !expanded;
			calcSize();
			parent.removeChild(this);
			parent.addChild(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta) {
		super.mouseOverComponent(mousePos, mouseWheelDelta);
		if (!mouseOverThisComponent(mousePos)) {
			expanded = false;
			calcSize();
		}
		return false;
	}

	@Override
	public void renderComponent(Vector2i position) {
		arrowLabel.renderForegroundColor = renderForegroundColor;
		currentItemLabel.renderForegroundColor = renderForegroundColor;
		GuiPrimitives.drawOutlinedRect(position.copy(), defaultSize.copy(), outlineSize, renderBackgroundColor,
				renderOutlineColor);
		GuiPrimitives.drawOutlinedRect(position.copy().add(new Vector2i(defaultSize.X - 20, 0)),
				new Vector2i(20, defaultSize.Y), outlineSize, renderBackgroundColor, renderOutlineColor);
	}

	@Override
	public void initComponent() {
		String textToDraw = selectedItem() != null ? selectedItem().itemName : "";
		currentItemLabel = new Label(this, new Vector2i(0, 0), defaultSize.copy().add(new Vector2i(-20, 0)),
				textToDraw);
		if (expanded) {
			arrowLabel = new Label(this, new Vector2i(defaultSize.X - 20, 0), new Vector2i(20, defaultSize.Y), "^");
			Vector2i buttonPos = new Vector2i(0, defaultSize.Y);
			for (ComboBoxItem item : items) {
				new Button(this, buttonPos.copy(), new Vector2i(defaultSize.X, 20), item.itemName, outlineSize,
						new Runnable() {

							@Override
							public void run() {
								selectIdx(items.indexOf(item));
								expanded = false;
								calcSize();
								setRenderColor(false);
							}
						});
				buttonPos.add(new Vector2i(0, 20));
			}
		} else {
			arrowLabel = new Label(this, new Vector2i(defaultSize.X - 20, 0), new Vector2i(20, defaultSize.Y), "v");
		}
		arrowLabel.useHighlights = false;
	}

}
