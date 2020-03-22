package de.oaknetwork.oaknetlink.api.gui.components;

import java.util.ArrayList;
import java.util.Arrays;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class TextPane extends ColorComponent{

	private String text="";
	private ArrayList<String> displayStrings=new ArrayList<String>();
	private int outlineSize=1;
	private int linePointer=0;
	
	public TextPane(Component parent, Vector2i position, Vector2i size, int outlineSize) {
		super(parent, position, size);
		this.outlineSize=outlineSize;
		useHighlights=false;
		initComponent();
	}
	
	private void formatText() {
		boolean linePointerToBottom=linePointer==displayStrings.size();
		displayStrings.clear();
		for(String line:text.split("\n")) {
			ArrayList<String> words=new ArrayList<String>(Arrays.asList(line.split(" ")));
			String lineToAdd="";
			while(words.size()>0) {
				if(MinecraftHooks.mcInterface.stringWidth(lineToAdd+words.get(0))>size().X-2*outlineSize-4) {
					displayStrings.add(lineToAdd);
					lineToAdd="";
				}
				lineToAdd+=words.get(0)+" ";
				words.remove(0);
			}
			displayStrings.add(lineToAdd);
		}
		if(linePointerToBottom)
			linePointer=displayStrings.size();
	}
	
	public void appendText(String text) {
		this.text+="\n"+text;
		formatText();
	}
	
	public void setText(String text) {
		this.text=text;
		formatText();
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta) {
		if(mouseOverThisComponent(mousePos)) {
			int possibleLineCount=size().Y/(MinecraftHooks.mcInterface.stringHeight()+2);
			if(mouseWheelDelta<0&&linePointer<displayStrings.size()) {
				linePointer++;
				return true;
			}else if(mouseWheelDelta>0&&linePointer-possibleLineCount>1) {
				linePointer--;
				return true;
			}
		}
		return super.mouseOverComponent(mousePos, mouseWheelDelta);
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void renderComponent(Vector2i position) {	
		GuiPrimitives.drawOutlinedRect(position, size(), outlineSize, renderBackgroundColor.copy().setAlpha(128),
				renderOutlineColor);
		Vector2i textPos=position.copy().add(new Vector2i(outlineSize+2, outlineSize+2));
		int possibleLineCount=size().Y/(MinecraftHooks.mcInterface.stringHeight()+2);
		int index=linePointer-possibleLineCount;
		if(index<1)
			index=1;
		while(index<linePointer&&textPos.Y-position.Y<size().Y-2*outlineSize-MinecraftHooks.mcInterface.stringHeight()) {
			GuiPrimitives.drawString(displayStrings.get(index-1), textPos.copy(), renderForegroundColor);
			index++;
			textPos.add(new Vector2i(0, MinecraftHooks.mcInterface.stringHeight()+2));
		}
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void initComponent() {
	}

}
