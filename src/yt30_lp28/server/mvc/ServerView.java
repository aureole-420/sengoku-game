package yt30_lp28.server.mvc;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Component;
import javax.swing.border.MatteBorder;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JList;

/**
 *Main mvc view
 * @param <TDropListItem> the type of item in the drop list
 */
public class ServerView<TDropListItem> extends JFrame {

	private static final long serialVersionUID = -9207739177112784038L;
	private JPanel contentPane;
	private final JPanel panel = new JPanel();
	private final JPanel panel_1 = new JPanel();
	private final JLabel lblUsername = new JLabel("User Name");
	private final JTextField textUsername = new JTextField();
	private final JLabel lblServerName = new JLabel("Server Addr");
	private final JTextField textServerName = new JTextField();
	private final JPanel panel_2 = new JPanel();
	private final JButton btnStart = new JButton("Send Team");
	private final JPanel panel_7 = new JPanel();
	private final JPanel panel_9 = new JPanel();
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	private IServerView2ModelAdapter view2ModelAdpt;
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTextArea txtDefaultInfo = new JTextArea();
	private final JComboBox<TDropListItem> comboBox = new JComboBox<>();
	private final JPanel panel_3 = new JPanel();
	DefaultListModel<TDropListItem> listModel = new DefaultListModel<>();
	private final JList<TDropListItem> list = new JList<>(listModel);
	private final JPanel panel_4 = new JPanel();
	private final JButton btnNewButton = new JButton("Send Game");
	private final JButton btnSendSengokuGame = new JButton("Send Sengoku Game");

	/**
	 * Create the frame.
	 * @param view2ModelAdpt adapter talking to main model
	 */
	public ServerView(IServerView2ModelAdapter view2ModelAdpt) {
		this.view2ModelAdpt = view2ModelAdpt;
		initGUI();
	}

	/**
	 * Start the view, i.e. set visibility to true
	 */
	public void start() {
		setVisible(true);
	}

	/**
	 * Add a tab in the tabbed panel
	 * @param title title of the tab
	 * @param component component to add
	 */
	public void addTab(String title, Component component) {
		tabbedPane.addTab(title, null, component, null);
	}

	/**
	 * Append a text in the view
	 * @param msg the text to append
	 */
	public void append(String msg) {
		txtDefaultInfo.append(msg + "\n");
	}

	/**
	 * Remote a tab in the tabbed panel
	 * @param component the component to remove
	 */
	public void removeTab(Component component) {
		tabbedPane.remove(component);
	}

	/** Set user name
	 * @param name the user name
	 */
	public void setUserName(String name) {
		textUsername.setText(name);
	}

	/**
	 * Set the server name
	 * @param localIP the servername(local ip address) to set
	 */
	public void setServerName(String localIP) {
		textServerName.setText(localIP);
	}

	/**
	 * Add an item in drop list
	 * @param item the item to add
	 */
	public void addConnected(TDropListItem item) {
		comboBox.insertItemAt(item, 0);
		listModel.addElement(item);
	}

	/**
	 * Remove an item in drop list
	 * @param item the item to remove
	 */
	public void removeConnected(TDropListItem item) {
		comboBox.removeItem(item);
		for (int i = 0; i < listModel.size(); i++) {
			if (listModel.getElementAt(i).equals(item)) {
				listModel.remove(i);
				break;
			}
		}
	}

	private void initGUI() {
		textServerName.setToolTipText("IP address");
		textServerName.setEditable(false);
		textServerName.setColumns(10);
		textUsername.setEditable(false);
		textUsername.setToolTipText("Input your name");
		textUsername.setText("name...");
		textUsername.setColumns(10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1400, 421);
		contentPane = new JPanel();
		contentPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

		contentPane.add(panel, BorderLayout.NORTH);

		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(2, 2, 0, 0));
		lblUsername.setToolTipText("username");

		panel_1.add(lblUsername);

		panel_1.add(textUsername);
		lblServerName.setToolTipText("server name");

		panel_1.add(lblServerName);

		panel_1.add(textServerName);

		panel.add(panel_2);
		btnStart.setToolTipText("Send Team");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//				view2ModelAdpt.startGame();
				view2ModelAdpt.sendTeams();
			}
		});

		panel_2.add(btnStart);

		panel.add(panel_7);

		panel_7.add(comboBox);

		panel.add(panel_4);
		btnNewButton.addActionListener(new ActionListener() { // send demo game
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.startGame();
			}
		});

		panel_4.add(btnNewButton);

		btnSendSengokuGame.addActionListener(new ActionListener() { // send sengokuGame
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.sendSengokuGame();
			}
		});

		panel.add(btnSendSengokuGame);

		contentPane.add(panel_9, BorderLayout.CENTER);
		panel_9.setLayout(new BorderLayout(0, 0));
		tabbedPane.setToolTipText("main panel");

		panel_9.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("Log Record", null, scrollPane, "log information");
		txtDefaultInfo.setToolTipText("Log information");

		scrollPane.setViewportView(txtDefaultInfo);

		tabbedPane.addTab("New tab", null, panel_3, null);
		panel_3.setLayout(new BorderLayout(0, 0));

		panel_3.add(list, BorderLayout.CENTER);
	}
}
