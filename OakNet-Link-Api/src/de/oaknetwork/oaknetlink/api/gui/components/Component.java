package de.oaknetwork.oaknetlink.api.gui.components;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.log.OakNetLinkLogProvider;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * CONCEPT Components
 * 
 * A component is a thing which can be placed in the OakNet-Link GUI System.
 * 
 * A component has a parent and it can have as many children as desired.
 * 
 * A component has a position and a size. The position is always relative to its
 * parent. This allows the creation of complex GuiComponents e.g. a button can
 * have a label as its child.
 * 
 * Each Component needs to derive from this base class.
 * 
 * Furthermore this class provides some abstract methods for easy
 * implementation.
 * 
 * It also provides some basic functionality for animations.
 * 
 * TODO develop a new concept for mouseOverThisComponent();
 * 
 * @author Fabian Fila
 */
public abstract class Component {

	static ExecutorService exec = Executors.newCachedThreadPool();

	/**
	 * The position of this component
	 */
	public Vector2i componentPosition;

	/**
	 * The size of this component
	 */
	protected Vector2i size;

	/**
	 * The children of this component
	 */
	protected ArrayList<Component> children = new ArrayList<Component>();

	/**
	 * the parent of this component
	 */
	protected Component parent;

	/**
	 * Creates a new Component
	 * 
	 * @param parent   the parent of this Component
	 * @param position the position of this Component relative to its parent
	 * @param size     the size of the Component
	 */
	public Component(Component parent, Vector2i position, Vector2i size) {
		this.parent = parent;
		this.componentPosition = position;
		this.size = size;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	/**
	 * This method will be called when the mouse is down on the screen
	 * 
	 * Use mouseOverThisComponent() to check if mouse is over this component
	 * 
	 * @param clickPos    the position where the mouse was clicked, relative to
	 *                    parent
	 * @param mouseButton 0 = Left click; 1 = Right click; 2 = Mouse wheel click
	 * @return true if the event should be canceled after this component. E.g. when
	 *         a button takes this event.
	 */
	public abstract boolean mouseDownComponent(Vector2i clickPos, int mouseButton);

	/**
	 * This method will be called when the mouse is released on the screen
	 * 
	 * Use mouseOverThisComponent() to check if mouse is over this component
	 * 
	 * @param clickPos    the position where the mouse was clicked, relative to
	 *                    parent
	 * @param mouseButton 0 = Left click; 1 = Right click; 2 = Mouse wheel click
	 * @return true if the event should be canceled after this component. E.g. when
	 *         a button takes this event.
	 */
	public abstract boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton);

	/**
	 * This method will be called when the mouse is hovered on the screen
	 * 
	 * Use mouseOverThisComponent() to check if mouse is over this component
	 * 
	 * @param mousePos        the position where the mouse was moved, relative to
	 *                        parent
	 * @param mouseWheelDelta if <0 the mouse wheel was moved in the upper
	 *                        direction, if >0 the mouse wheel was moved down
	 * @return true if the event should be canceled after this component. E.g. when
	 *         a button takes this event.
	 */
	public abstract boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta);

	/**
	 * This method will be called when the component is drawn.
	 * 
	 * Here you should draw GUIPrimitives
	 * 
	 * @param position where the component should be drawn.
	 */
	public abstract void renderComponent(Vector2i position);

	/**
	 * This method will be called parent component is resized.
	 * 
	 * It also needs to be called at the end of the constructor.
	 * 
	 * Here you can create child components which are required for the component
	 */
	public abstract void initComponent();

	/**
	 * Get the parent component
	 * 
	 * @return the parent componen
	 */
	public Component parent() {
		return parent;
	}

	/**
	 * Use this to add a child to the component
	 * 
	 * @param childToAdd
	 */
	public void addChild(Component childToAdd) {
		if (children.contains(childToAdd))
			removeChild(childToAdd);
		children.add(childToAdd);
	}

	/**
	 * Use this to remove a child of the component
	 * 
	 * @param childToRemove
	 */
	public void removeChild(Component childToRemove) {
		children.remove(childToRemove);
	}

	/**
	 * Use this to resize the component
	 * 
	 * Note all children need to be reinitialized
	 * 
	 * @param newSize
	 */
	public void resize(Vector2i newSize) {
		size = newSize;
		sizeChanged();
	}

	/**
	 * Returns the components size
	 * 
	 * @return the components size
	 */
	public Vector2i size() {
		return size.copy();
	}

	/**
	 * Will be called when the Component was resized
	 */
	protected void sizeChanged() {
		children.clear();
		initComponent();
	}

	/**
	 * Calling this method will move the component in transformationVector direction
	 * for duration time
	 * 
	 * @param transformationVector
	 * @param duration             in 0.01s
	 */
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

	/**
	 * Use this to check if some coordinates are over this component
	 * 
	 * @param mousePos
	 * @return true or false
	 */
	public boolean mouseOverThisComponent(Vector2i mousePos) {
		return mousePos.X > 0 && mousePos.Y > 0 && mousePos.X < size.X && mousePos.Y < size.Y;
	}

	/*
	 * Region GUI System start
	 */
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
	/*
	 * Region GUI System End
	 */
}
