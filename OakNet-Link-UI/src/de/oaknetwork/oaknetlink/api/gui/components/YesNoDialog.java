package de.oaknetwork.oaknetlink.api.gui.components;

import de.oaknetwork.oaknetlink.api.utils.Vector2i;

/**
 * This dialog asks the user for a yes / no decision
 * 
 * @author Fabian Fila
 */
public class YesNoDialog extends Dialog {

	private Runnable onNo;
	private Runnable onYes;

	private boolean yesPressed = false;

	/**
	 * Create a new yes / no Dialog and displays it to the User
	 * 
	 * @param outlineSize the thickness of the outline
	 * @param title       the string which will be shown in the titlebar
	 * @param message     the message which is displayed to the user
	 * @param blocking    decides if the dialog blocks all other input
	 * @param onNo        a Runnable which will be executed when the no button is
	 *                    pressed
	 * @param onYes       a Runnable which will be executed when the yes button is
	 *                    pressed
	 */
	public YesNoDialog(int outlineSize, String title, String message, boolean blocking, Runnable onNo, Runnable onYes) {
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
		if (!yesPressed)
			onNo.run();
	}
}
