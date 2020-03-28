package de.oaknetwork.oaknetlink.api.gui.structure;

import de.oaknetwork.oaknetlink.api.gui.backend.IWorldSelectionResult;
import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.Color;
import de.oaknetwork.oaknetlink.api.gui.components.Component;
import de.oaknetwork.oaknetlink.api.gui.components.List;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * This class defines the structure of the WorldSelectionDialog
 * 
 * @author Fabian Fila
 */
public class WorldSelectionDialog extends Window{

	private IWorldSelectionResult result;
	
	private static final int margin = 2;
	private static final int outlineSize = 1;
	
	public WorldSelectionDialog(Component parent, IWorldSelectionResult result) {
		super(parent, parent.size().divide(2).add(new Vector2i(230, 250).divide(2).negate()), new Vector2i(230, 250), 1, "Select World", true);
		this.result=result;
		topmost=true;
	}
	
	@Override
	public void close() {
		super.close();
		result.run("");
	}
	
	@Override
	public boolean mouseDown(Vector2i clickPos, int mouseButton) {
		super.mouseDown(clickPos, mouseButton);
		return true;
	}
	
	@Override
	public void initComponent() {
		super.initComponent();
		List worldSelectionList = new List<WorldSelectionEntry>(this, new Vector2i(margin, margin), contentSize().add(new Vector2i(-2*margin, -3*margin-20)), outlineSize);
		worldSelectionList.inheritColorsFromParent=false;
		worldSelectionList.setOutlineColor(Color.WHITE);
		Button closeButton = new Button(this, contentSize().add(new Vector2i(-100-margin, -20-margin)), new Vector2i(100, 20), "Close", outlineSize, new Runnable() {
			
			@Override
			public void run() {
				close();
			}
		});
		Button okButton = new Button(this, new Vector2i(margin, contentSize().Y-margin-20), new Vector2i(100, 20), "Choose World", outlineSize, new Runnable() {
	
			@Override
			public void run() {
				close();
			}
		});
	}

}
