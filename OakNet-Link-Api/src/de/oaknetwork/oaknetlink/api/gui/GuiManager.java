package de.oaknetwork.oaknetlink.api.gui;

import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.gui.components.Component;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class GuiManager {

	static ArrayList<Component> components = new ArrayList<Component>();
	
	public static void render() {
		for(Component component : components) {
			component.render(new Vector2i(0, 0));
		}
	}
	
	public static boolean click(Vector2i clickPosition, int mouseButton) {
		for(int i = components.size(); i>0; i--) {
			if(components.get(i-1).click(clickPosition, mouseButton))
				return true;
		}
		return false;
	}
	
	public static boolean mouseOver(Vector2i mousePos) {
		for(int i = components.size(); i>0; i--) {
			if(components.get(i-1).mouseOver(mousePos))
				return true;
		}
		return false;
	}
	
	public static boolean keyPressed(char key, int keyCode) {
		for(int i = components.size(); i>0; i--) {
			if(components.get(i-1).keyPressed(key, keyCode))
				return true;
		}
		return false;
	}	
	
	public static void addComponent(Component componentToAdd) {
		components.add(componentToAdd);
	}
	
	public static void removeComponent(Component componentToRemove) {
		components.remove(componentToRemove);
	}
}
