package de.oaknetwork.oaknetlink.api.gui;

import java.util.ArrayList;

import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.CheckBox;
import de.oaknetwork.oaknetlink.api.gui.components.Color;
import de.oaknetwork.oaknetlink.api.gui.components.ComboBox;
import de.oaknetwork.oaknetlink.api.gui.components.ComboBoxItem;
import de.oaknetwork.oaknetlink.api.gui.components.Component;
import de.oaknetwork.oaknetlink.api.gui.components.ComponentContainer;
import de.oaknetwork.oaknetlink.api.gui.components.Dialog;
import de.oaknetwork.oaknetlink.api.gui.components.List;
import de.oaknetwork.oaknetlink.api.gui.components.ListEntry;
import de.oaknetwork.oaknetlink.api.gui.components.TextBox;
import de.oaknetwork.oaknetlink.api.gui.components.TextPane;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.mcinterface.MinecraftHooks;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class GuiManager extends ComponentContainer {

	private static Vector2i currentSize = new Vector2i(0, 0);
	private static GuiManager instance;
	private static Overlay overlay;

	public GuiManager() {
		super(null, new Vector2i(0, 0), new Vector2i(0, 0));
		instance = this;
		overlay = new Overlay(this, new Vector2i(0, 0), new Vector2i(0, 0));
		removeChild(overlay);
	}

	@Override
	public void render(Vector2i position) {
		if (currentSize.X != MinecraftHooks.mcInterface.screenWidth()
				|| currentSize.Y != MinecraftHooks.mcInterface.screenHeight()) {
			resize(new Vector2i(MinecraftHooks.mcInterface.screenWidth(), MinecraftHooks.mcInterface.screenHeight()));
			currentSize = new Vector2i(MinecraftHooks.mcInterface.screenWidth(),
					MinecraftHooks.mcInterface.screenHeight());
			overlay.resize(currentSize);
		}
		super.render(position);
	}

	@Override
	public void initComponent() {
		Button button = new Button(getInstance(), new Vector2i(size().X - 102, 2), new Vector2i(100, 20), "OakNet-Link",
				1, new Runnable() {

					@Override
					public void run() {
						addChild(overlay);
					}
				});
		button.setBackgroundColor(Color.DARKGRAY);
		button.setBackgroundColorHighlight(Color.LIGHTGRAY);
		button.setForegroundColorHighlight(Color.BLACK);
		button.setOutlineColor(Color.BLACK);
		button.setOutlineColorHighlight(Color.BLACK);
		/**
		 * Button button2 = new Button(getInstance(), new Vector2i(120, 2), new
		 * Vector2i(100, 20), "Open Dialog close!", 1, new Runnable() {
		 * 
		 * @Override public void run() { Dialog window = new Dialog(1, "Dialog Test",
		 *           "Das ist ein Dialogfenster\nund es hat zwei Zeilen.", true, true);
		 *           } });
		 * 
		 *           Button button3 = new Button(this, new Vector2i(120, 32), new
		 *           Vector2i(100, 20), "Open Dialog!", 1, new Runnable() {
		 * 
		 * @Override public void run() { Dialog window = new Dialog(1, "Dialog Test",
		 *           "Das ist ein Dialogfenster\nund es l‰sst sich nicht schlieﬂen.",
		 *           false, false); } });
		 * 
		 *           TextBox textBox = new TextBox(getInstance(), new Vector2i(20, 50),
		 *           new Vector2i(150, 20), 1, "Text");
		 * 
		 *           CheckBox checkBox = new CheckBox(getInstance(), new Vector2i(20,
		 *           80), new Vector2i(150, 20), 1, false, "das ist eine Checkbox");
		 * 
		 *           List<ListEntry> list = new List<ListEntry>(getInstance(), new
		 *           Vector2i(20, 110), new Vector2i(150, 100), 1);
		 * 
		 *           TextPane textPane = new TextPane(getInstance(), new Vector2i(190,
		 *           20), new Vector2i(150, 120), 1); textPane.setText( "Lorem ipsum
		 *           dolor sit amet, vel oratio semper molestiae ei, no has eruditi
		 *           neglegentur.\nNe periculis erroribus eam, mea ex deleniti
		 *           comprehensam. Eu usu novum sententiae, ne cum illud debet nemore.
		 *           Vis cibo voluptatibus cu, oportere ullamcorper pro id.\n\nIn quo
		 *           ipsum delenit qualisque, eam mentitum torquatos ad. Ne unum
		 *           adolescens constituam qui, ne sed viris laoreet omnesque. Qui sale
		 *           dicunt adipiscing id, cu iisque scripta interpretaris eum.");
		 * 
		 *           ComboBox comboBox = new ComboBox(getInstance(), new Vector2i(350,
		 *           20), new Vector2i(150, 20), 1); comboBox.addItem(new
		 *           ComboBoxItem("Item 1")); comboBox.addItem(new ComboBoxItem("Item
		 *           2")); comboBox.addItem(new ComboBoxItem("Item 3"));
		 **/
	}

	@Override
	public void resize(Vector2i newSize) {
		// we have to keep the opened Windows
		ArrayList<Window> windows = new ArrayList<Window>();
		for (Component child : children) {
			try {
				((Window) child).componentPosition = newSize.copy().divide(2)
						.add(child.size().copy().divide(2).negate());
				windows.add((Window) child);
			} catch (ClassCastException e) {
				continue;
			}
		}
		super.resize(newSize);
		for (Window window : windows) {
			addChild(window);
		}
	}

	public static GuiManager getInstance() {
		return instance;
	}
}
