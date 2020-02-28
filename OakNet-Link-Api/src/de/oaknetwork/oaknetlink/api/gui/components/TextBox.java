package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class TextBox extends ColorComponent {

	public String text;

	private boolean activated = false;
	private int outlineSize;
	private long lastBlink;
	private String drawText;

	public TextBox(Component parent, Vector2i position, Vector2i size, int outlineSize, String defaultText) {
		super(parent, position, size);
		text = defaultText;
		this.outlineSize=outlineSize;
		lastBlink = System.currentTimeMillis();
	}

	@Override
	public boolean clickComponent(Vector2i clickPos, int mouseButton) {
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
		text += key;
		return true;
	}

	@Override
	public void renderComponent(Vector2i position) {
		GuiPrimitives.drawOutlinedRect(position, size, outlineSize, renderBackgroundColor.copy().setAlpha(128), renderOutlineColor);
		drawText = text;
		if (System.currentTimeMillis() - lastBlink > 1000)
			lastBlink = System.currentTimeMillis();
		else if (System.currentTimeMillis() - lastBlink > 500 && activated)
			drawText += "|";
		Vector2i textPos = position.copy().add(size.copy().divide(2)).add(new Vector2i(5-(size.X/2), -(MinecraftHooks.mcInterface.stringHeight()/2)));
		GuiPrimitives.drawString(drawText, textPos, renderForegroundColor);

	}

}
