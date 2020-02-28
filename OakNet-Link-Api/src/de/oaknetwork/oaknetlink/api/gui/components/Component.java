package de.oaknetwork.oaknetlink.api.gui.components;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.log.OakNetLinkLogProvider;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public abstract class Component {
	
	static ExecutorService exec = Executors.newCachedThreadPool();
	
	Vector2i componentPosition;
	Vector2i size;
	ArrayList<Component> children = new ArrayList<Component>(); 
	Component parent;
	
	public Component(Component parent, Vector2i position, Vector2i size) {
		this.parent = parent;
		this.componentPosition = position;
		this.size = size;
		if(parent!=null) {
			parent.addChild(this);
		}
	}
	
	public abstract boolean clickComponent(Vector2i clickPos, int mouseButton);
	
	public abstract boolean mouseOverComponent(Vector2i mousePos);
	
	public abstract void renderComponent(Vector2i position);
	
	public void render(Vector2i position) {
		Vector2i newPos = position.copy().add(componentPosition);
		renderComponent(newPos);
		for(Component child : children) {
			child.render(newPos);
		}
	}

	public boolean click(Vector2i clickPos, int mouseButton) {
		Vector2i newPos = clickPos.copy().add(componentPosition.copy().negate());
		for(int i = children.size(); i>0; i--) {
			if(children.get(i-1).click(newPos, mouseButton))
				return true;
		}
		return clickComponent(newPos, mouseButton);
	}
	
	public boolean mouseOver(Vector2i mousePos) {
		Vector2i newPos = mousePos.copy().add(componentPosition.copy().negate());
		for(int i = children.size(); i>0; i--) {
			if(children.get(i-1).mouseOver(newPos))
				return true;
		}
		return mouseOverComponent(newPos);
	}
	
	public boolean keyPressed(char key, int keyCode) {
		for(int i = children.size(); i>0; i--) {
			if(children.get(i-1).keyPressed(key, keyCode))
				return true;
		}
		return false;
	}
	
	public Vector2i componentPosition() {
		return componentPosition;
	}
	
	public void setPosition(Vector2i newPosition) {
		componentPosition = newPosition;
	}
	
	public Vector2i size() {
		return size;
	}
	
	public void setSize(Vector2i newSize) {
		this.size = newSize;
	}
	
	public Component parent() {
		return parent;
	}
	
	public void addChild(Component childToAdd) {
		children.add(childToAdd);
	}
	
	public void removeChild(Component childToRemove) {
		children.remove(childToRemove);
	}
	
	// calling this method will move the component in transformationVector direction
	// for duration time (in 0.01s)
	public void transformAnimation(Vector2i transformationVector, int duration) {
		transformationVector.divide(duration);
		exec.execute(new Runnable() {
			
			@Override
			public void run() {
				int dur=duration;
				while(dur>0) {
					componentPosition.add(transformationVector);
					dur--;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
				}
				
			}
		});
	}
	
	boolean mouseOverThisComponent(Vector2i mousePos) {
		return mousePos.X > 0 && mousePos.Y > 0 && mousePos.X < size.X && mousePos.Y < size.Y;
	}
}
