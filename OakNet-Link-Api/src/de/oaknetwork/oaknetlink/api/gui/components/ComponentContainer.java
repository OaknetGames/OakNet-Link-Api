package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class ComponentContainer extends Component {

	public ComponentContainer(Component parent, Vector2i position, Vector2i size) {
		super(parent, position, size);
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta) {
		return false;
	}

	@Override
	public void renderComponent(Vector2i position) {
	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void initComponent() {		
	}

}
