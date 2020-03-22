package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

// This class has to be overridden for a list entry, the width of the entry will be calculated from the parent list.
public abstract class ListEntry extends ColorComponent{

	public boolean selected=false;
	
	public ListEntry(int height) {
		super(null, new Vector2i(0, 0), new Vector2i(0, height));
	}
	
	public void setParent(Component newParent) {
		if(newParent==null)
			parent.removeChild(this);
		else{
			newParent.addChild(this);
		}
		parent=newParent;
	}
	
	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		if(!mouseOverThisComponent(clickPos)) 
			return false;
		try {
			List parentList=(List) parent;
			parentList.deselectAll();
			selected=true;
		}catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public void renderComponent(Vector2i position) {
		if(selected)
			GuiPrimitives.drawRect(position, size(), renderForegroundColor);		
	}
}
