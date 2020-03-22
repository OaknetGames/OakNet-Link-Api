package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class YesNoDialog extends Dialog {

	private Runnable onNo;
	private Runnable onYes;

	private boolean yesPressed = false;

	public YesNoDialog(int outlineSize, String title, String message, boolean blocking, Runnable onNo,
			Runnable onYes) {
		super(outlineSize, title, message, true, "No", blocking);
		this.onNo = onNo;
		this.onYes = onYes;
	}

	@Override
	public void initComponent() {
		super.initComponent();
		new Button(this, contentSize().add(new Vector2i(-outlineSize - 105, -outlineSize - 20)), new Vector2i(50, 20),
				"Yes", outlineSize, new Runnable() {

					@Override
					public void run() {
						yesPressed = true;
						close();
						onYes.run();
					}
				});
	}
	
	@Override
	public void close() {
		super.close();
		if(!yesPressed)
			onNo.run();
	}
}
