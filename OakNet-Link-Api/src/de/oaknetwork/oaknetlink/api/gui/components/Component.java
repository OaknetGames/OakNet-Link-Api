package de.oaknetwork.oaknetlink.api.gui.components;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.log.OakNetLinkLogProvider;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public abstract class Component {

	static ExecutorService exec = Executors.newCachedThreadPool();

	public Vector2i componentPosition;
	
	protected Vector2i size;

	protected ArrayList<Component> children = new ArrayList<Component>();
	protected Component parent;

	public Component(Component parent, Vector2i position, Vector2i size) {
		this.parent = parent;
		this.componentPosition = position;
		this.size = size;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public abstract boolean mouseDownComponent(Vector2i clickPos, int mouseButton);
	
	public abstract boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton);

	public abstract boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta);

	public abstract void renderComponent(Vector2i position);
	
	public abstract void initComponent();

	public void render(Vector2i position) {
		Vector2i newPos = position.copy().add(componentPosition);
		renderComponent(newPos);
		for (Component child : new ArrayList<Component>(children)) {
			child.render(newPos.copy());
		}
	}

	public boolean mouseDown(Vector2i clickPos, int mouseButton) {
		Vector2i newPos = clickPos.copy().add(componentPosition.copy().negate());
		for (int i = children.size(); i > 0; i--) {
			if (children.get(i - 1).mouseDown(newPos.copy(), mouseButton))
				return true;
		}
		return mouseDownComponent(newPos.copy(), mouseButton);
	}
	
	public boolean mouseReleased(Vector2i clickPos, int mouseButton) {
		Vector2i newPos = clickPos.copy().add(componentPosition.copy().negate());
		for (int i = children.size(); i > 0; i--) {
			if (children.get(i - 1).mouseReleased(newPos.copy(), mouseButton))
				return true;
		}
		return mouseReleasedComponent(newPos.copy(), mouseButton);
	}

	public boolean mouseOver(Vector2i mousePos, int mouseWheelDelta) {
		Vector2i newPos = mousePos.copy().add(componentPosition.copy().negate());
		for (int i = children.size(); i > 0; i--) {
			if (children.get(i - 1).mouseOver(newPos.copy(), mouseWheelDelta))
				return true;
		}
		return mouseOverComponent(newPos.copy(), mouseWheelDelta);
	}

	public boolean keyPressed(char key, int keyCode) {
		for (int i = children.size(); i > 0; i--) {
			if (children.get(i - 1).keyPressed(key, keyCode))
				return true;
		}
		return false;
	}

	public Component parent() {
		return parent;
	}

	public void addChild(Component childToAdd) {
		if(children.contains(childToAdd))
			removeChild(childToAdd);
		children.add(childToAdd);
	}

	public void removeChild(Component childToRemove) {
		children.remove(childToRemove);
	}
	
	public void resize(Vector2i newSize) {
		size=newSize;
		sizeChanged();
	}
	
	public Vector2i size() {
		return size.copy();
	}
	
	protected void sizeChanged() {
		children.clear();
		initComponent();
	}

	// calling this method will move the component in transformationVector direction
	// for duration time (in 0.01s)
	public void transformAnimation(Vector2i transformationVector, int duration) {
		transformationVector.divide(duration);
		exec.execute(new Runnable() {

			@Override
			public void run() {
				int dur = duration;
				while (dur > 0) {
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

	public boolean mouseOverThisComponent(Vector2i mousePos) {
		return mousePos.X > 0 && mousePos.Y > 0 && mousePos.X < size.X && mousePos.Y < size.Y;
	}
}
