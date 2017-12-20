package yt30_lp28.chatapp.mvc.view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.JList;
import javax.swing.JTabbedPane;

/**
 * Mini mvc view
 * @param <T> type of the item in JList
 */
public class MiniView<T> extends JPanel {
	private static final long serialVersionUID = 3488894404641634048L;
	protected static final Component MiniView = null;
	private final JPanel panel = new JPanel();
	private final JTextField textField = new JTextField();
	private final JButton btnSendText = new JButton("Text");
	private final JButton btnSendImage = new JButton("Image");
	private final JPanel panel_1 = new JPanel();
	private final JButton btnLeave = new JButton("Leave");
	private final JScrollPane scrolablePane = new JScrollPane();

	private MiniView2ModelAdapter miniView2ModelAdpt;
	private final JTextPane textPane = new JTextPane();
	private DefaultListModel<T> listModel = new DefaultListModel<T>();
	private final JList<T> list = new JList<T>(listModel);
	private final JButton btnSendComp = new JButton("Non-Scroll Comp");
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	/**
	 * Create the panel.
	 * @param miniView2ModelAdpt adapter talking to model
	 */
	public MiniView(MiniView2ModelAdapter miniView2ModelAdpt) {
		this.miniView2ModelAdpt = miniView2ModelAdpt;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout(0, 0));

		add(panel, BorderLayout.SOUTH);
		textField.setToolTipText("Input text");
		textField.setColumns(20);

		panel.add(textField);
		btnSendText.setToolTipText("Send text");
		btnSendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miniView2ModelAdpt.sendText(textField.getText());
			}
		});

		panel.add(btnSendText);
		btnSendImage.setToolTipText("Send image");
		btnSendImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = openFile();
				if (file == null) {
					return;
				}
				ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
				miniView2ModelAdpt.sendImage(imageIcon);
			}
		});

		panel.add(btnSendImage);
		btnSendComp.setToolTipText("Send a component");

		btnSendComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("[MiniView.sendComp()] SwingUtilities.isEventDispatchThread() "
						+ SwingUtilities.isEventDispatchThread());
				miniView2ModelAdpt.sendComp();
			}
		});

		panel.add(btnSendComp);
		panel_1.setToolTipText("User list");
		panel_1.setBorder(new TitledBorder(null, "User List", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BorderLayout(0, 0));

		panel_1.add(list, BorderLayout.NORTH);
		btnLeave.setToolTipText("Leave the chat room");
		panel_1.add(btnLeave, BorderLayout.SOUTH);
		btnLeave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miniView2ModelAdpt.leave(MiniView.this);
			}
		});
		add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addTab("ScrollablePane", null, scrolablePane, "scrollable panel for normal message");
		scrolablePane.setPreferredSize(new Dimension(50, 100));
		textPane.setToolTipText("Messages");

		scrolablePane.setViewportView(textPane);
	}

	/**
	 * Append a text in the view
	 * @param str the text to append
	 */
	public void append(String str) {
		StyledDocument document = (StyledDocument) textPane.getDocument();
		try {
			document.insertString(document.getLength(), str + "\n", null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private File openFile() {
		// Open a file
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false); // for single file selection
		int returnVal = fileChooser.showOpenDialog(MiniView);
		File f = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = fileChooser.getSelectedFile();
		}
		return f;
	}

	/**
	 * Add an item in the list
	 * @param t the item to add
	 */
	public void addItem(T t) {
		listModel.addElement(t);
	}

	/**
	 * Remote an item in the list
	 * @param t the item to remove
	 */
	public void removeItem(T t) {
		for (int i = 0; i < listModel.size(); i++) {
			if (listModel.getElementAt(i).toString().equals(t.toString())) {
				listModel.remove(i);
			}
		}
	}

	/**
	 * Add a scrollable component in the view
	 * @param comp the component to add
	 * @param label the label with the component
	 */
	public void addScrollableComponent(Component comp, String label) {
		append("install a scrollable component in text panel:  " + label + "\n");
		textPane.insertComponent(comp);
	}

	/**
	 * Add a non-scrollable component in the view
	 * @param comp the component to add
	 * @param label the label with the component
	 */
	public void addNonScrollableComponent(Component comp, String label) {
		append("Install a non-scrollable component: " + label + "\n");
		tabbedPane.addTab(label, null, comp, label);
	}
}
