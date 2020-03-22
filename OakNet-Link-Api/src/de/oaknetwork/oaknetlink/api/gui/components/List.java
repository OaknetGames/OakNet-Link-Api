package de.oaknetwork.oaknetlink.api.gui.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;

import de.oaknetwork.oaknetlink.api.gui.GuiPrimitives;
import de.oaknetwork.oaknetlink.api.log.Logger;
import de.oaknetwork.oaknetlink.api.log.OakNetLinkLogProvider;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class List<T extends ListEntry> extends ColorComponent {

	private ArrayList<T> entries = new ArrayList<T>();
	private ArrayList<ArrayList<T>> sites = new ArrayList<ArrayList<T>>();
	private int outlineSize = 1;
	private int entriesPerSite = 1;
	private int currentSite = 1;
	private Label siteLabel;

	public List(Component parent, Vector2i position, Vector2i size, int outlineSize) {
		super(parent, position, size);
		this.outlineSize = outlineSize;
		useHighlights = false;
		initComponent();
	}

	private void updateCurrentSite() {
		if (entries.size() != 0) {
			entries.forEach((entry) -> entry.setParent(null));
			Vector2i entrypos = new Vector2i(outlineSize, outlineSize);
			for (T entry : sites.get(currentSite - 1)) {
				entry.setParent(this);
				entry.resize(new Vector2i(size().X - 2 * outlineSize, entry.size().Y));
				entry.componentPosition = entrypos.copy();
				entrypos.add(new Vector2i(0, entry.size().Y));

			}
		}
	}

	public void addEntry(T entry) {
		entries.add(entry);
		addEntryToSites(entry);
	}

	private void addEntryToSites(T entry) {
		if (sites.size() == 0) {
			sites.add(new ArrayList<T>());
			sites.get(0).add(entry);
		}
		int sizeOfSite = 0;
		for (T siteEntry : sites.get(sites.size() - 1)) {
			sizeOfSite += siteEntry.size().Y;
		}
		if (sizeOfSite + entry.size().Y < size().Y - 3 * outlineSize - 20)
			sites.get(sites.size() - 1).add(entry);
		else {
			sites.add(new ArrayList<T>());
			sites.get(sites.size() - 1).add(entry);
		}
	}

	public void removeEntry(T entry) {
		if (entries.contains(entry)) {
			entries.remove(entry);
			sites.clear();
			entries.forEach((entryToAdd) -> addEntryToSites(entry));
		}
	}

	public ArrayList<T> getEntries() {
		return (ArrayList<T>) Collections.unmodifiableList(entries);
	}

	public void deselectAll() {
		entries.forEach((entry) -> entry.selected = false);
	}

	// returns null if nothing is selected
	public T getSelected() {
		T result = null;
		for (T entry : entries) {
			if (entry.selected)
				result = entry;
		}
		return result;
	}

	@Override
	public boolean mouseDownComponent(Vector2i clickPos, int mouseButton) {
		return mouseOverThisComponent(clickPos);
	}

	@Override
	public boolean mouseOverComponent(Vector2i mousePos, int mouseWheelDelta) {
		return false;
	}

	@Override
	public void renderComponent(Vector2i position) {
		siteLabel.text = currentSite + " / " + sites.size();
		GuiPrimitives.drawOutlinedRect(position, size(), outlineSize, renderBackgroundColor.copy().setAlpha(128),
				renderOutlineColor);
		GuiPrimitives.drawRect(position.copy().add(new Vector2i(0, size().Y - 2 * outlineSize - 20)),
				new Vector2i(size().X, outlineSize), renderOutlineColor);
		if (sites.size() == 0)
			return;
		if (currentSite > sites.size())
			currentSite = 1;

	}

	@Override
	public boolean mouseReleasedComponent(Vector2i clickPos, int mouseButton) {
		return false;
	}

	@Override
	public void initComponent() {
		siteLabel = new Label(this, new Vector2i(0, size().Y - 20 - 2 * outlineSize),
				new Vector2i(size().X, 20 + 2 * outlineSize), "0 / 0");
		siteLabel.useHighlights = false;
		new Button(this, new Vector2i(outlineSize, size().Y - 20 - outlineSize), new Vector2i(20, 20), "<-",
				outlineSize, new Runnable() {

					@Override
					public void run() {
						if (currentSite > 1)
							currentSite--;
						updateCurrentSite();
					}
				});
		new Button(this, new Vector2i(size().X - 20 - outlineSize, size().Y - 20 - outlineSize), new Vector2i(20, 20),
				"->", outlineSize, new Runnable() {

					@Override
					public void run() {
						if (currentSite < sites.size())
							currentSite++;
						updateCurrentSite();
					}
				});
	}

}
