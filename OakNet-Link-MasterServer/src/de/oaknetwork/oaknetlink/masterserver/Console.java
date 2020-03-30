package de.oaknetwork.oaknetlink.masterserver;

import java.awt.BorderLayout;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/**
 * This class creates the ConsoleWindow for the Master-Server
 * 
 * @author Fabian Fila
 */
public class Console extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6138764497367126844L;
	private JPanel contentPane;
	private static Console frame;
	static PrintStream someOtherStream;
	static PrintStream someOtherErrStream;
	static PrintStream oldStream;
	static PrintStream oldErrStream;

	/**
	 * Launch the application.
	 */
	public static void open() {
		try {
			frame = new Console();
			frame.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public Console() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 300);
		setTitle("OakNet-Link MasterServer");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);

		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		oldStream = System.out;
		oldErrStream = System.err;
		someOtherStream = new PrintStream(System.out) {

			@Override
			public void println(String s) {
				textArea.append(s + "\n");
				textArea.setCaretPosition(textArea.getText().length());
				System.setOut(oldStream);
				System.out.println(s);
				System.setOut(Console.someOtherStream);
			}
		};

		someOtherErrStream = new PrintStream(System.err) {

			@Override
			public void println(String s) {
				textArea.append(s + "\n");
				textArea.setCaretPosition(textArea.getText().length());
				System.setErr(oldErrStream);
				System.err.println(s);
				System.setErr(Console.someOtherErrStream);
			}
		};

		// System.out umlenken:
		System.setOut(someOtherStream);
		System.setErr(someOtherErrStream);
	}
}
