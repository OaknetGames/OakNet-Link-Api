package de.oaknetwork.oaknetlink.api.gui.components;

import java.util.ArrayList;
import java.util.Arrays;

import de.oaknetwork.oaknetlink.api.gui.GuiManager;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class Dialog extends Window {

	public boolean blocking = false;

	private String closeButtonText = "Ok";
	private ArrayList<String> linesToDraw;
	private boolean closeable = true;
	
	protected int outlineSize = 1;
	

	public Dialog(int outlineSize, String title, String message, boolean closeable,
			String closeButtonText, boolean blocking) {
		super(GuiManager.getInstance(), GuiManager.getInstance().size().divide(2), new Vector2i(100, 100), outlineSize, title, closeable);
		this.blocking = blocking;
		this.closeable = closeable;
		this.outlineSize = outlineSize;
		this.closeButtonText = closeButtonText;
		topmost = true;
		formatMessage(message);
		calcSize();
		componentContainer.children.clear();
		addComponents();
	}

	public Dialog(int outlineSize, String title, String message, boolean closeable,
			boolean blocking) {
		this(outlineSize, title, message, closeable, "Ok", blocking);
	}

	public void setMessage(String message) {
		formatMessage(message);
		calcSize();
		componentContainer.children.clear();
		addComponents();
	}

	private void formatMessage(String message) {
		linesToDraw = new ArrayList<String>(Arrays.asList(message.split("\n")));
	}

	private void calcSize() {
		int maxWidth = 4;
		for (String line : linesToDraw) {
			maxWidth = MinecraftHooks.mcInterface.stringWidth(line) + 4 > maxWidth
					? MinecraftHooks.mcInterface.stringWidth(line) + 4
					: maxWidth;
		}
		maxWidth = maxWidth < 100 ? 100 : maxWidth;
		int maxHeight = closeable ? 24 : 4;
		maxHeight += linesToDraw.size() * (MinecraftHooks.mcInterface.stringHeight() + 2);
		resize(new Vector2i(maxWidth + 2 * outlineSize, maxHeight + 3 * outlineSize + 20));
		componentPosition = parent.size().divide(2).add(size().divide(2).negate());
	}

	private void addComponents() {
		Vector2i labelPos = new Vector2i(2, 2);
		for (String line : linesToDraw) {
			new Label(this, labelPos.copy(),
					new Vector2i(size().X - 4 - 2 * outlineSize, MinecraftHooks.mcInterface.stringHeight()),
					line).useHighlights = false;
			labelPos.add(new Vector2i(0, MinecraftHooks.mcInterface.stringHeight() + 2));
		}
		if (closeable)
			new Button(this, contentSize().add(new Vector2i(-outlineSize - 50, -outlineSize - 20)),
					new Vector2i(50, 20), closeButtonText, outlineSize, new Runnable() {

						@Override
						public void run() {
							close();

						}
					});
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		return super.mouseDownComponent(clickPos, mouseButton) || blocking;
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return super.mouseReleasedComponent(clickPos, mouseButton) || blocking;
	}

	@Override
	public boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta) {
		return super.mouseOverComponent(mousePos, mouseWheelDelta) || blocking;
	}
}
