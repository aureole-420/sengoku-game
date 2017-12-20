package yt30_lp28.chatapp.impl;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * MyPanel is a simple panel as a non-scrollable component 
 */
public class MyPanel extends JPanel {
	private static final long serialVersionUID = 8347134929155606266L;
	private final JPanel panel = new JPanel();
	private final JTextField textField = new JTextField();
	private final JButton btnNewButton = new JButton("Click");
	private final JPanel panel_1 = new JPanel();
	private final JTextArea textArea = new JTextArea();

	/**
	 * Create the panel.
	 */
	public MyPanel() {
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		add(panel, BorderLayout.NORTH);
		textField.setColumns(10);
		panel.add(textField);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("-------------" + textField.getText());
				textArea.append(textField.getText());
			}
		});

		panel.add(btnNewButton);
		add(panel_1, BorderLayout.SOUTH);
		textArea.setColumns(10);
		textArea.setRows(10);
		panel_1.add(textArea);
	}
}
