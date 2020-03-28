package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.log.OakNetLinkLogProvider;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * This Component can be used to let user input text in a box.
 * 
 * @author Fabian Fila
 */
public class TextBox extends ColorComponent {

	/**
	 * The text of the Textbox
	 */
	public String text;

	private boolean activated = false;
	private int outlineSize;
	private long lastBlink;
	private String drawText;
	private String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "ôóòâàáûúùîìíêèéÔÓÒÂÁÀÛÚÙÍÌÎÈÉÊ" + "äöüÄÖÜ" + "1234567890" + "!\"§$%&/()=?ß´`*+~'#-_1²³\\{}[]°" + " ";

	/**
	 * Creates a new TextBox
	 * 
	 * @param parent      the parent of the component
	 * @param position    the position relative to its parent
	 * @param size        the size of the component
	 * @param outlineSize the thickness of the outline
	 * @param defaultText the default text
	 */
	public TextBox(Component parent, Vector2i position, Vector2i size, int outlineSize, String defaultText) {
		super(parent, position, size);
		text = defaultText;
		this.outlineSize = outlineSize;
		lastBlink = System.currentTimeMillis();
		initComponent();
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		if (mouseOverThisComponent(clickPos)) {
			activated = true;
			return true;
		}
		activated = false;
		return false;
	}

	@Override
	public boolean keyPressed(char key, int keyCode) {
		if (!activated)
			return super.keyPressed(key, keyCode);
		if (keyCode == 14)
			text = text.length() > 0 ? text.substring(0, text.length() - 1) : "";
		if (allowedChars.contains(key + ""))
			if (MinecraftHooks.mcInterface.stringWidth(text + key + "") <= size().X - 10)
				text += key + "";
			else
				return false;
		return true;
	}

	@Override
	public void renderComponent(Vector2i position) {
		GuiPrimitives.drawOutlinedRect(position, size(), outlineSize, renderBackgroundColor, renderOutlineColor);
		drawText = text;
		if (System.currentTimeMillis() - lastBlink > 1000)
			lastBlink = System.currentTimeMillis();
		else if (System.currentTimeMillis() - lastBlink > 500 && activated)
			drawText += "|";
		Vector2i textPos = position.copy().add(size().divide(2))
				.add(new Vector2i(5 - (size().X / 2), -(MinecraftHooks.mcInterface.stringHeight() / 2)));
		GuiPrimitives.drawString(drawText, textPos, renderForegroundColor);

	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void initComponent() {
	}

}
