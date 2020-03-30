package de.oaknetwork.oaknetlink.api.gui.structure;

import de.oaknetwork.oaknetlink.api.gui.backend.IWorldSelectionResult;
import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.ComboBox;
import de.oaknetwork.oaknetlink.api.gui.components.ComboBoxItem;
import de.oaknetwork.oaknetlink.api.gui.components.Component;
import de.oaknetwork.oaknetlink.api.gui.components.Label;
import de.oaknetwork.oaknetlink.api.gui.components.TextBox;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * This class defines the structure of the CreateServerWindow
 * 
 * @author Fabian Fila
 */
public class CreateServerWindow extends Window {

	private static final int margin = 2;
	private static final int outlineSize = 1;

	public CreateServerWindow(Component parent) {
		super(parent, new Vector2i(0, 0), new Vector2i(300, 201), outlineSize, "Create new Server", true);
	}

	@Override
	public void initComponent() {
		super.initComponent();
		Vector2i leftColSize = new Vector2i((contentSize().X - 3 * margin) / 3, 20);
		Vector2i rightColSize = new Vector2i(((contentSize().X - 3 * margin) / 3) * 2, 20);
		Vector2i leftColPos = new Vector2i(margin, margin);
		Vector2i rightColPos = new Vector2i(2 * margin + 1 + (contentSize().X - 3 * margin) / 3, margin);

		Label serverNameLabel = new Label(this, leftColPos.copy(), leftColSize, "Server Name: ");
		serverNameLabel.centered = false;
		serverNameLabel.useHighlights = false;
		new TextBox(this, rightColPos.copy(), rightColSize, outlineSize, "Username's Game");
		leftColPos.add(new Vector2i(0, margin + 20));
		rightColPos.add(new Vector2i(0, margin + 20));

		Label serverPasswortLabel = new Label(this, leftColPos.copy(), leftColSize, "Server Password: ");
		serverPasswortLabel.centered = false;
		serverPasswortLabel.useHighlights = false;
		new TextBox(this, rightColPos.copy(), rightColSize, outlineSize, "");
		leftColPos.add(new Vector2i(0, margin + 20));
		rightColPos.add(new Vector2i(0, margin + 20));

		Label serverTypeLabel = new Label(this, leftColPos.copy(), leftColSize, "Server Type: ");
		serverTypeLabel.centered = false;
		serverTypeLabel.useHighlights = false;
		ComboBox comboBoxType = new ComboBox(this, rightColPos.copy(), rightColSize, outlineSize);
		comboBoxType.addItem(new ComboBoxItem("Vanilla"));
		comboBoxType.addItem(new ComboBoxItem("Forge"));
		leftColPos.add(new Vector2i(0, margin + 20));
		rightColPos.add(new Vector2i(0, margin + 20));

		Label gameModeLabel = new Label(this, leftColPos.copy(), leftColSize, "Game Mode: ");
		gameModeLabel.centered = false;
		gameModeLabel.useHighlights = false;
		ComboBox comboBoxGameMode = new ComboBox(this, rightColPos.copy(), rightColSize, outlineSize);
		comboBoxGameMode.addItem(new ComboBoxItem("Survival"));
		comboBoxGameMode.addItem(new ComboBoxItem("Creative"));
		leftColPos.add(new Vector2i(0, margin + 20));
		rightColPos.add(new Vector2i(0, margin + 20));

		Label difficultyLabel = new Label(this, leftColPos.copy(), leftColSize, "Difficulty: ");
		difficultyLabel.centered = false;
		difficultyLabel.useHighlights = false;
		ComboBox comboBoxDifficulty = new ComboBox(this, rightColPos.copy(), rightColSize, outlineSize);
		comboBoxDifficulty.addItem(new ComboBoxItem("Peaceful"));
		comboBoxDifficulty.addItem(new ComboBoxItem("Easy"));
		comboBoxDifficulty.addItem(new ComboBoxItem("Normal"));
		comboBoxDifficulty.addItem(new ComboBoxItem("Hard"));
		leftColPos.add(new Vector2i(0, margin + 20));
		rightColPos.add(new Vector2i(0, margin + 20));

		Label worldLabel = new Label(this, leftColPos.copy(), rightColSize, "World: No world selected");
		worldLabel.centered = false;
		worldLabel.useHighlights = false;
		new Button(this, leftColPos.copy().add(new Vector2i(rightColSize.X + margin + 1, 0)),
				leftColSize, "Select World", outlineSize, new Runnable() {

					@Override
					public void run() {
						new WorldSelectionDialog(parent(), new IWorldSelectionResult() {

							@Override
							public void run(String worldName) {
								worldLabel.text = "World: " + worldName;
							}
						});
					}
				});
		leftColPos.add(new Vector2i(0, margin + 20));
		rightColPos.add(new Vector2i(0, margin + 20));

		Label worldInfoLabel = new Label(this, leftColPos.copy(), new Vector2i(contentSize().X - 2 * margin, 20),
				"If no world is selected, a new world will be created.");
		worldInfoLabel.useHighlights = false;
		leftColPos.add(new Vector2i(0, margin + 20));
		rightColPos.add(new Vector2i(0, margin + 20));

		new Button(this,
				leftColPos.copy().add(new Vector2i(((contentSize().X - 3 * margin) / 2) + margin, 0)),
				new Vector2i((contentSize().X - 3 * margin) / 2, 20), "Start Server", outlineSize, new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

					}
				});
		leftColPos.add(new Vector2i(0, margin + 20));
		rightColPos.add(new Vector2i(0, margin + 20));

	}

}
