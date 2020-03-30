package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * A simple Button, what should I say?
 * 
 * @author Fabian Fila
 */
public class Button extends ColorComponent {

	private Label buttonTextLabel;
	private int outlineSize;
	private Runnable click;
	private String buttonText = "";

	/**
	 * Creates a new Button
	 * 
	 * @param parent      the parent component
	 * @param position    the position of this component, relative to its parent
	 * @param size        the size of the component
	 * @param buttonText  the text which will be drawn on the Button
	 * @param outlineSize the thickness of the outline
	 * @param click       a Runnable which will be executed on click
	 */
	public Button(Component parent, Vector2i position, Vector2i size, String buttonText, int outlineSize,
			Runnable click) {
		super(parent, position, size);
		this.outlineSize = outlineSize;
		this.click = click;
		this.buttonText = buttonText;
		initComponent();
	}

	/**
	 * Sets the Button text
	 * 
	 * @param text the text to set
	 */
	public void setButtonText(String text) {
		buttonText = text;
		buttonTextLabel.text = text;
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		if (mouseOverThisComponent(clickPos)) {
			click.run();
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta) {
		super.mouseOverComponent(mousePos, mouseWheelDelta);
		return false;
	}

	@Override
	public void renderComponent(Vector2i position) {
		GuiPrimitives.drawOutlinedRect(position, size(), outlineSize, renderBackgroundColor, renderOutlineColor);
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void initComponent() {
		buttonTextLabel = new Label(this, new Vector2i(0, 0), size(), buttonText);

	}

}
