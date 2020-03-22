package de.oaknetwork.oaknetlink.api.gui.components;

import java.util.concurrent.Callable;
import java.util.function.Function;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class Button extends ColorComponent{
	
	private Label buttonTextLabel;
	private int outlineSize;
	private Runnable click;
	private String buttonText="";

	public Button(Component parent, Vector2i position, Vector2i size, String buttonText, int outlineSize, Runnable click) {
		super(parent, position, size);
		this.outlineSize = outlineSize;
		this.click = click;
		this.buttonText=buttonText;
		initComponent();
	}
	
	public void setButtonText(String text) {
		buttonText=text;
		buttonTextLabel.text=text;
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		if(mouseOverThisComponent(clickPos)) {
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
