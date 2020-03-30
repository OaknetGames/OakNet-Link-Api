package de.oaknetwork.oaknetlink.api.log;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

/**
 * The Window containing some TextAreas for logging
 * 
 * @author Fabian Fila
 */
public class LogWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4552258156990333427L;

	private JPanel contentPane;

	private JTextArea mcLogArea;
	private JTextArea oakNetLinkLogArea;
	private JTextArea mcServerLogArea;

	private static LogWindow instance;

	/**
	 * Open the frame.
	 */
	public static void open() {
		try {
			LogWindow frame = new LogWindow();
			frame.setVisible(true);
			instance = frame;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public LogWindow() {
		setTitle("OakNet-Link Log Window");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 3, 5, 0));

		JLabel lblMinecraftLog = new JLabel("Minecraft Log: ");
		panel.add(lblMinecraftLog);

		JLabel lblOaknetlinkLog = new JLabel("OakNet-Link Log:");
		panel.add(lblOaknetlinkLog);

		JLabel lblMinecraftServerLog = new JLabel("Minecraft Server Log:");
		panel.add(lblMinecraftServerLog);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(0, 3, 5, 5));

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);

		mcLogArea = new JTextArea();
		mcLogArea.setEditable(false);
		scrollPane.setViewportView(mcLogArea);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1);

		oakNetLinkLogArea = new JTextArea();
		oakNetLinkLogArea.setEditable(false);
		scrollPane_1.setViewportView(oakNetLinkLogArea);

		JScrollPane scrollPane_2 = new JScrollPane();
		panel_1.add(scrollPane_2);

		mcServerLogArea = new JTextArea();
		mcServerLogArea.setEditable(false);
		scrollPane_2.setViewportView(mcServerLogArea);

	}

	public JTextArea getMcLogArea() {
		return mcLogArea;
	}

	public JTextArea getMcServerLogArea() {
		return mcServerLogArea;
	}

	public JTextArea getOakNetLinkLogArea() {
		return oakNetLinkLogArea;
	}

	public static LogWindow getInstance() {
		return instance;
	}

}
