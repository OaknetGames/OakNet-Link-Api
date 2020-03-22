package de.oaknetwork.oaknetlink.api.gui;

import de.oaknetwork.oaknetlink.api.gui.components.Button;
import de.oaknetwork.oaknetlink.api.gui.components.Component;
import de.oaknetwork.oaknetlink.api.gui.components.TextBox;
import de.oaknetwork.oaknetlink.api.gui.components.TextPane;
import de.oaknetwork.oaknetlink.api.gui.components.Window;
import de.oaknetwork.oaknetlink.api.utils.Vector2i;

public class ChatWindow extends Window{
	
	private static final int margin = 2;
	private static final int outlineSize = 1;
	
	public ChatWindow(Component parent) {
		super(parent, new Vector2i(0, 0), new Vector2i(400, 200), 1, "Chat", true);
	}
	
	@Override
	public void initComponent() {
		super.initComponent();
		TextPane chatPane = new TextPane(this, new Vector2i(margin, margin), contentSize().add(new Vector2i(-2*margin, -3*margin-20)), outlineSize);
		TextBox chatBox = new TextBox(this, new Vector2i(margin, contentSize().Y-margin-20), new Vector2i(contentSize().X-3*margin-100, 20), outlineSize, "Message");
		Button sendButton = new Button(this, contentSize().add(new Vector2i(-margin-100, -margin-20)), new Vector2i(100, 20), "Send", outlineSize, new Runnable() {
			
			@Override
			public void run() {
				
			}
		});
		
	}
}
