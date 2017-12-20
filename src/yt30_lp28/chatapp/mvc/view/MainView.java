package yt30_lp28.chatapp.mvc.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
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
import javax.swing.JTextPane;

/**
 *Main mvc view
 * @param <TDropListItem> the type of item in the drop list
 */
public class MainView<TDropListItem> extends JFrame {

	private static final long serialVersionUID = -9207739177112784038L;
	private JPanel contentPane;
	private final JPanel panel = new JPanel();
	private final JPanel panel_1 = new JPanel();
	private final JLabel lblUsername = new JLabel("Username");
	private final JTextField textUsername = new JTextField();
	private final JLabel lblServerName = new JLabel("Server Name");
	private final JTextField textServerName = new JTextField();
	private final JPanel panel_2 = new JPanel();
	private final JButton btnStart = new JButton("Start");
	private final JPanel panel_3 = new JPanel();
	private final JTextField txtCRName = new JTextField();
	private final JButton btnCreateCR = new JButton("Create");
	private final JPanel panel_4 = new JPanel();
	private final JPanel panel_5 = new JPanel();
	private final JPanel panel_6 = new JPanel();
	private final JTextField textField = new JTextField();
	private final JButton btnConnect = new JButton("Connect to");
	private final JComboBox<TDropListItem> comboBox = new JComboBox<TDropListItem>();
	private final JButton btnRequest = new JButton("Request");
	private final JPanel panel_7 = new JPanel();
	private final JButton btnQuit = new JButton("Leave All & Quit");
	private final JPanel panel_9 = new JPanel();
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	private MainView2ModelAdapter<TDropListItem> mainView2ModelAdpt;
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTextArea txtDefaultInfo = new JTextArea();
	private final JScrollPane scrollPane_1 = new JScrollPane();
	private final JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
	private final JTextPane textPane = new JTextPane();
	private final JPanel panel_8 = new JPanel();
	private final JTextField textField_1 = new JTextField();
	private final JButton btnSendText = new JButton("Send Text");
	private final JButton btnSendComp = new JButton("Send Comp");
	private final JComboBox<TDropListItem> comboBox_1 = new JComboBox<>();

	/**
	 * Create the frame.
	 * @param mainView2ModelAdpt adapter talking to main model
	 */
	public MainView(MainView2ModelAdapter<TDropListItem> mainView2ModelAdpt) {
		this.mainView2ModelAdpt = mainView2ModelAdpt;
		initGUI();
	}

	/**
	 * Create the frame
	 */
	public MainView() {
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
		comboBox_1.insertItemAt(item, 0);
	}

	/**
	 * Remove an item in drop list
	 * @param item the item to remove
	 */
	public void removeConnected(TDropListItem item) {
		comboBox.removeItem(item);
		comboBox_1.removeItem(item);
	}

	/**
	 * Append a user level message
	 * @param str the message string
	 * @param name the label
	 */
	public void appendUserMsg(String str, String name) {
		System.out.println("append user msg : " + str + " name : " + name);
		//		textPane.add(new JLabel(name + ": "+str));
		//		validate();
		//		repaint();
		//		textPane.app
		StyledDocument document = (StyledDocument) textPane.getDocument();
		try {
			document.insertString(document.getLength(), name + " : " + str + "\n", null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a user level scrollable component
	 * @param comp the component to add
	 * @param label the label
	 */
	public void addScroll(Component comp, String label) {
		textPane.insertComponent(comp);
	}

	/**
	 * Add a user level non-scrollable component
	 * @param comp the component to add
	 * @param label the label
	 */
	public void addNonScroll(Component comp, String label) {
		tabbedPane_1.add(label, comp);
	}

	private void initGUI() {
		textField.setToolTipText("remote IP");
		textField.setColumns(10);
		txtCRName.setToolTipText("Input chatroom name");
		txtCRName.setText("chatroom name");
		txtCRName.setColumns(8);
		textServerName.setToolTipText("IP address");
		textServerName.setEditable(false);
		textServerName.setColumns(10);
		textUsername.setToolTipText("Input your name");
		textUsername.setText("name...");
		textUsername.setColumns(6);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 457);
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
		btnStart.setToolTipText("Log In");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainView2ModelAdpt.startServer(textUsername.getText().trim(), textServerName.getText().trim());
			}
		});

		panel_2.add(btnStart);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Create Chat Room",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		panel.add(panel_3);
		panel_3.setLayout(new GridLayout(2, 1, 0, 0));

		panel_3.add(txtCRName);
		btnCreateCR.setToolTipText("Create chatroom");
		btnCreateCR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainView2ModelAdpt.createChatRoom(txtCRName.getText());
			}
		});

		panel_3.add(btnCreateCR);
		panel_4.setBorder(new TitledBorder(null, "Remote", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(1, 2, 0, 0));
		panel_5.setBorder(new TitledBorder(null, "Remote IP", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panel_4.add(panel_5);
		panel_5.setLayout(new GridLayout(2, 1, 0, 0));

		panel_5.add(textField);
		btnConnect.setToolTipText("Connect to remote user");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainView2ModelAdpt.connectTo(textField.getText());
			}
		});

		panel_5.add(btnConnect);
		panel_6.setBorder(new TitledBorder(null, "Remote Hosts", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panel_4.add(panel_6);
		panel_6.setLayout(new GridLayout(3, 1, 0, 0));
		comboBox.setToolTipText("Connected users");
		comboBox.setMaximumRowCount(10);

		panel_6.add(comboBox);
		btnRequest.setToolTipText("Request chatrooms from remote user");

		panel_6.add(btnRequest);
		btnRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainView2ModelAdpt.request(comboBox.getItemAt(comboBox.getSelectedIndex()));
			}
		});

		panel.add(panel_7);
		btnQuit.setToolTipText("Quit the app");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainView2ModelAdpt.stop();
			}
		});

		panel_7.add(btnQuit);

		contentPane.add(panel_9, BorderLayout.CENTER);
		panel_9.setLayout(new BorderLayout(0, 0));
		tabbedPane.setToolTipText("main panel");

		panel_9.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("Log Record", null, scrollPane, "log information");
		txtDefaultInfo.setToolTipText("Log information");

		scrollPane.setViewportView(txtDefaultInfo);

		tabbedPane.addTab("User Chat", null, scrollPane_1, null);

		scrollPane_1.setViewportView(tabbedPane_1);

		tabbedPane_1.addTab("ScrollMsg", null, textPane, null);

		contentPane.add(panel_8, BorderLayout.SOUTH);

		panel_8.add(comboBox_1);
		textField_1.setColumns(15);
		panel_8.add(textField_1);
		btnSendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idx = comboBox_1.getSelectedIndex();
				idx = idx == -1 ? 0 : idx;
				mainView2ModelAdpt.sendMsg(textField_1.getText(), comboBox_1.getItemAt(idx));
				System.out.println("User send msg----");
			}
		});
		panel_8.add(btnSendText);
		btnSendComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idx = comboBox_1.getSelectedIndex();
				idx = idx == -1 ? 0 : idx;
				mainView2ModelAdpt.sendComp(comboBox_1.getItemAt(idx));
			}
		});
		panel_8.add(btnSendComp);
	}
}
