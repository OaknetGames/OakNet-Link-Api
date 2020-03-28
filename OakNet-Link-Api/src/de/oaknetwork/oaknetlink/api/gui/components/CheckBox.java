package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * A GuiComponent that can be checked.
 * 
 * @author Fabian Fila
 */
public class CheckBox extends ColorComponent {

	/**
	 * is the CheckBox checked?
	 */
	public boolean checked;

	private String label;
	private Button checkBoxButton;
	private Label checkBoxLabel;
	private int outlineSize = 1;

	/**
	 * Creates a new CheckBox
	 * 
	 * @param parent      the parent of the component
	 * @param position    the position relative to its parent
	 * @param size        the size of the component
	 * @param outlineSize the thickness of the outline
	 * @param isChecked   is the CheckBox checked by default?
	 * @param label       the label which is drawn right next to the CheckBox
	 */
	public CheckBox(Component parent, Vector2i position, Vector2i size, int outlineSize, boolean isChecked,
			String label) {
		super(parent, position, size);
		this.checked = isChecked;
		this.label = label;
		this.outlineSize = outlineSize;
		initComponent();
	}

	/**
	 * Sets the CheckBox's label
	 * 
	 * @param label the text to set
	 */
	public void setLabel(String label) {
		checkBoxLabel.text = label;
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void renderComponent(Vector2i position) {
	}

	@Override
	public boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta) {
		return false;
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void initComponent() {
		checkBoxButton = new Button(this, new Vector2i(0, 0), new Vector2i(size().Y, size().Y), checked ? "x" : "",
				outlineSize, new Runnable() {

					@Override
					public void run() {
						checked = !checked;
						checkBoxButton.setButtonText(checked ? "x" : "");
					}
				});
		checkBoxLabel = new Label(this, new Vector2i(size().Y + 1, 0),
				new Vector2i(size().X - (size().Y + 1), size().Y), label);
		checkBoxLabel.useHighlights = false;

	}

}
