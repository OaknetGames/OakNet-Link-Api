package de.oaknetwork.oaknetlink.api.gui.components;

import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * A window can be dragged and closed.
 * 
 * It contains a ComponentContainer which will be used for each component which
 * is added to the window.
 * 
 * The window can be set topmost, so it will be always rendered above anything.
 * 
 * @author Fabian Fila
 */
public class Window extends ColorComponent {

	/**
	 * Decides if the Window is topmost
	 */
	public boolean topmost = false;

	private String title;
	private int outlineSize = 1;
	private Label titleLabel;
	private boolean closeable = false;

	private Vector2i dragVector;
	private boolean dragging = false;

	protected ComponentContainer componentContainer;

	/**
	 * Creates an new Window
	 * 
	 * @param parent      the parent of the component
	 * @param position    the position relative to its parent
	 * @param size        the size of the component
	 * @param outlineSize the thickness of the outline
	 * @param title       the string which is showed in the titlebar
	 * @param closeable   decides if the window can be closed
	 */
	public Window(Component parent, Vector2i position, Vector2i size, int outlineSize, String title,
			boolean closeable) {
		super(parent, position, size);
		this.outlineSize = outlineSize;
		this.title = title;
		this.closeable = closeable;
		useHighlights = false;
		initComponent();
	}

	/**
	 * Brings the window in the foreground
	 */
	public void setOnTop() {
		parent.removeChild(this);
		parent.addChild(this);
	}

	/**
	 * Closes the window
	 */
	public void close() {
		parent.removeChild(this);
	}

	/**
	 * Receives the size of the ComponentContainer 
	 * 
	 * @return the size
	 */
	public Vector2i contentSize() {
		return componentContainer.size().copy();
	}

	@Override
	public void addChild(Component childToAdd) {
		if (componentContainer == null)
			super.addChild(childToAdd);
		else
			componentContainer.addChild(childToAdd);
	}

	@Override
	public void removeChild(Component childToRemove) {
		if (componentContainer == null)
			super.addChild(childToRemove);
		else
			componentContainer.removeChild(childToRemove);
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		if (mouseOverThisComponent(clickPos)) {
			setOnTop();
			dragging = true;
			dragVector = clickPos;
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		if (dragging) {
			dragging = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta) {
		if (dragging) {
			Vector2i deltaDrag = mousePos.copy().add(dragVector.copy().negate());
			componentPosition.add(deltaDrag);
			return true;
		}
		return mouseOverThisComponent(mousePos);
	}

	@Override
	public void renderComponent(Vector2i position) {
		if (topmost)
			setOnTop();
		GuiPrimitives.drawOutlinedRect(position, size(), outlineSize, renderBackgroundColor.copy().setAlpha(200),
				renderOutlineColor);
		GuiPrimitives.drawOutlinedRect(position, new Vector2i(size().X, 2 * outlineSize + 20), outlineSize,
				renderBackgroundColor.copy().setAlpha(0), renderOutlineColor);
	}

	@Override
	public void initComponent() {
		ArrayList<Component> components = null;
		if (componentContainer != null) {
			components = componentContainer.children;
			componentContainer = null;
		}
		if (closeable) {
			new Button(this, new Vector2i(size().X - outlineSize - 20, outlineSize), new Vector2i(20, 20), "x",
					outlineSize, new Runnable() {

						@Override
						public void run() {
							close();
						}
					});
			titleLabel = new Label(this, new Vector2i(0, 0),
					new Vector2i(size().X - outlineSize - 20, 2 * outlineSize + 20), this.title);
		} else {
			titleLabel = new Label(this, new Vector2i(0, 0), new Vector2i(size().X, 2 * outlineSize + 20), this.title);
		}
		titleLabel.useHighlights = false;
		componentContainer = new ComponentContainer(this, new Vector2i(outlineSize, 2 * outlineSize + 20),
				new Vector2i(size().X - 2 * outlineSize, size().Y - 3 * outlineSize - 20));
		if (components != null)
			componentContainer.children = components;
	}

}
