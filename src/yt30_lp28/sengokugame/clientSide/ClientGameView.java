package yt30_lp28.sengokugame.clientSide;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import map.IRightClickAction;

import yt30_lp28.sengokugame.serverSide.InterTeamStringBackToServerMsg;

import yt30_lp28.sengokugame.serverSide.IntraTeamStringMsg;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.layers.Layer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import java.awt.Dimension;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.SwingConstants;

/**
 * The client game view 
 * @author yt30, lp28
 *
 * @param <CBType> the data type for combo boxes
 */
public class ClientGameView<CBType> extends JFrame {

	private static final long serialVersionUID = -2849215799862701652L;
	private JPanel contentPane;
	private MyMapPanel _mapPanel;
	private final JPanel _pnlCtrl = new JPanel();
	private final JPanel _pnlDecision = new JPanel();
	private final JLabel _lblFromCity = new JLabel("Base City");
	private final JComboBox<CBType> _cbFromCity = new JComboBox<CBType>();
	private final JLabel _lblToCity = new JLabel("Target City (Migrate or Attack)");
	private final JComboBox<CBType> _cbToCity = new JComboBox<CBType>();
	private final JLabel _lblNumCitizens = new JLabel("Num of Soldiers to Send");
	private final JButton _btnSubmitDecision = new JButton("Take Action");

	private IClientGameView2ModelAdapter<CBType> _gameModel;
	private final JTextField _tfNoCtz2Move = new JTextField();
	private final JPanel panel = new JPanel();
	private final JTextField txtField = new JTextField();
	private final JSplitPane splitPane = new JSplitPane();
	private final JPanel panel_1 = new JPanel();
	private final JTextPane textPane = new JTextPane();
	private final JScrollPane scrollPane = new JScrollPane();
	private final JPanel _pnlNotation = new JPanel();
	private final JLabel lblTeam1 = new JLabel("Team1: human players");
	private final JLabel lblTeam2 = new JLabel("Team2: human players");
	private final JLabel lblTeam0 = new JLabel("Team0: creeps (computer)");
	private final JLabel lblNumOfSoldiersInBaseLabel = new JLabel("Num of Soldiers in Base");
	private final JLabel labelNumSoldierInBase = new JLabel("0");
	private final JPanel pnlGameResult = new JPanel();
	private final JLabel lblWinner = new JLabel("Winner of the Game:");
	private final JLabel lblWinnerIs = new JLabel("?");

	private DefaultComboBoxModel<CBType> CBModelCityTo;
	private DefaultComboBoxModel<CBType> CBModelCityFrom;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGameView frame = new ClientGameView(null, null, null);
					frame.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientGameView(IClientGameView2ModelAdapter<CBType> gm, IRightClickAction rightClick,
			ILeftClickAction leftClick) {
		_tfNoCtz2Move.setText("100");
		_tfNoCtz2Move.setColumns(10);
		_gameModel = gm;
		initGUI();
		// left/right click action
		_mapPanel.addRightClickAction(rightClick);
		_mapPanel.addLeftClickAction(leftClick);
	}

	/**
	 * init the gui.
	 */
	private void initGUI() {
		createContentPanel();

		createControlPanel();
		_pnlCtrl.setBackground(Color.WHITE);

		getContentPane().add(_pnlCtrl, BorderLayout.NORTH);

		contentPane.add(splitPane, BorderLayout.CENTER);

		_mapPanel = new MyMapPanel(Earth.class);

		splitPane.setRightComponent(_mapPanel);
		_mapPanel.setPreferredSize(new java.awt.Dimension(600, 400));

		_mapPanel.addRightClickAction(new IRightClickAction() {
			@Override
			public void apply(Position p) {
				System.out.println("[RightClickEvent()]: " + p);
			}
		});

		_mapPanel.addLeftClickAction(new ILeftClickAction() {

			@Override
			public void apply(Position p) {
				System.out.println("[ILeftClickAction()]: " + p);
			}

		});

		panel_1.setPreferredSize(new Dimension(180, 10));
		panel_1.setBorder(new TitledBorder(null, "ChatRoom", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		panel_1.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(textPane);
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
				"Enter your message (press ENTER to send intra-team msg; SHIFT+ENTER to send inter-team msg)",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setLayout(new BorderLayout(0, 0));
		txtField.setToolTipText(
				"Enter your message here -- press ENTER to send intrateam msg; press SHIFT+ENTER to send interteam msg.");

		panel.add(txtField);

		txtField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, java.awt.event.InputEvent.SHIFT_DOWN_MASK),
				"shiftEnterAction");

		txtField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterAction");

		txtField.getActionMap().put("enterAction", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Pressed Enter ....");
				System.out.println(txtField.getText());
				_gameModel.sendToChatRoom(IntraTeamStringMsg.class, new IntraTeamStringMsg(txtField.getText()));
			}
		});

		txtField.getActionMap().put("shiftEnterAction", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Pressed Shift + Enter ....");
				System.out.println(txtField.getText());
				_gameModel.sendInterTeamMsg(InterTeamStringBackToServerMsg.class,
						new InterTeamStringBackToServerMsg(txtField.getText()));
			}
		});

	}

	/**
	 * create the content panel
	 */
	public void createContentPanel() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 1000, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

	/**
	 * create the control panel;
	 */
	public void createControlPanel() {
		_pnlCtrl.setBorder(new TitledBorder(null, "Control Panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_pnlNotation.setBackground(Color.WHITE);
		_pnlNotation.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Team Notation",
				TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));

		_pnlCtrl.add(_pnlNotation);
		_pnlNotation.setLayout(new GridLayout(3, 1));
		lblTeam1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTeam1.setBackground(Color.CYAN);
		lblTeam1.setOpaque(true);
		//lblTeam;

		_pnlNotation.add(lblTeam1);
		lblTeam2.setBackground(Color.YELLOW);
		lblTeam2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTeam2.setOpaque(true);

		_pnlNotation.add(lblTeam2);
		lblTeam0.setForeground(Color.WHITE);
		lblTeam0.setBackground(Color.GRAY);
		lblTeam0.setOpaque(true);

		_pnlNotation.add(lblTeam0);
		_pnlDecision.setBackground(Color.WHITE);
		_pnlCtrl.add(_pnlDecision, BorderLayout.CENTER);
		_pnlDecision.setLayout(new GridLayout(4, 2));
		_pnlDecision.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Strategy Panel",
				TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));

		_pnlDecision.add(_lblFromCity);
		_cbFromCity.addActionListener(new ActionListener() { // combo box listener
			public void actionPerformed(ActionEvent e) {
				if (_cbFromCity.getSelectedIndex() < 0) {
					return;
				}
				labelNumSoldierInBase.setText(
						"" + _gameModel.getNumSoldiersInBase(_cbFromCity.getItemAt(_cbFromCity.getSelectedIndex())));
				System.out.println(
						"[ClientGameView() Item Selected ]" + _cbFromCity.getItemAt(_cbFromCity.getSelectedIndex()));
				CBType baseCity = _cbFromCity.getItemAt(_cbFromCity.getSelectedIndex());
				_gameModel.updateRangeLayer(baseCity);
			}
		});
		_pnlDecision.add(_cbFromCity);
		_pnlDecision.add(_lblToCity);
		_pnlDecision.add(_cbToCity);

		CBModelCityTo = new DefaultComboBoxModel<CBType>();
		CBModelCityFrom = new DefaultComboBoxModel<CBType>();
		_cbToCity.setModel(CBModelCityTo);
		_cbFromCity.setModel(CBModelCityFrom);

		_lblNumCitizens.setToolTipText("Num of Citizens to move");
		_pnlDecision.add(_lblNumCitizens);
		_pnlDecision.add(_tfNoCtz2Move);

		_pnlDecision.add(_tfNoCtz2Move);

		_pnlDecision.add(lblNumOfSoldiersInBaseLabel);
		labelNumSoldierInBase.setHorizontalAlignment(SwingConstants.CENTER);

		_pnlDecision.add(labelNumSoldierInBase);
		_btnSubmitDecision.addActionListener(new ActionListener() { // submit decision
			public void actionPerformed(ActionEvent e) {

				if (_cbFromCity.getSelectedIndex() < 0 || _cbToCity.getSelectedIndex() < 0) {
					System.out.println("<<ClientGameViw.submitDecisionButton>> exception parsing a string to integer");
					return;
				}
				int noCtz2Move = 0;
				try {
					noCtz2Move = Integer.parseInt(_tfNoCtz2Move.getText().trim());
				} catch (Exception ex) {
					System.out.println(
							"<<ClientGameViw.submitDecisionButton>> exception parsing a string to integer. Please enter a valid integer!");
					ex.printStackTrace();
					return;
				}
				_gameModel.sendDecision(_cbFromCity.getSelectedItem(), _cbToCity.getSelectedItem(), noCtz2Move);
				// !!!这里应该判断一下decision是�?�有效
				// (2) 起始城市是�?�有部署
				// (2)调动士兵数是�?�在�?许范围内。

			}
		});

		_pnlCtrl.add(_btnSubmitDecision);
		pnlGameResult.setBackground(Color.WHITE);

		pnlGameResult.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Game Result",
				TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));

		_pnlCtrl.add(pnlGameResult);
		pnlGameResult.setLayout(new GridLayout(2, 1, 0, 0));

		pnlGameResult.add(lblWinner);
		lblWinnerIs.setBackground(Color.WHITE);
		lblWinnerIs.setHorizontalAlignment(SwingConstants.CENTER);

		pnlGameResult.add(lblWinnerIs);
		lblWinnerIs.setOpaque(true);
	}

	/**
	 * start the gui
	 */
	public void start() {
		_mapPanel.start();
		this.setVisible(true);
	}

	/**
	 * add layer to the map panel
	 * @param layer the layer to add
	 */
	public void addLayer(Layer layer) {
		_mapPanel.addLayer(layer);
	}

	/**
	 * remove layer from the map panel;
	 * @param layer the layer to remove
	 */
	public void removeLayer(Layer layer) {
		_mapPanel.removeLayer(layer);
	}

	/*
	public void addCityIcon(CBType cityIcon) {
		_cbFromCity.insertItemAt(cityIcon, 0);
		_cbFromCity.setSelectedIndex(0);
		
		_cbToCity.insertItemAt(cityIcon, 0);
		_cbToCity.setSelectedIndex(0);
	
	}
	*/

	/**
	 * Get the Combo box model for the base city combo box
	 * @return the combo box model
	 */
	public DefaultComboBoxModel<CBType> getCBModelCityTo() {
		return CBModelCityTo;
	}

	/**
	 * Get the combo box model for the target city combo box
	 * @return the combo box model
	 */
	public DefaultComboBoxModel<CBType> getCBModelCityFrom() {
		return CBModelCityFrom;
	}

	/*
	public void removeFromCityAll() {
		_cbFromCity.removeAllItems();
	}
	
	public void addFromCityCB(CBType cityIcon) {
		_cbFromCity.insertItemAt(cityIcon, 0);
		// _cbFromCity.setSelectedIndex(0);
	}
	
	public void addToCityCB(CBType cityIcon) {
		_cbToCity.insertItemAt(cityIcon, 0);
		System.out.println("ClientGameview.addToCityCB(): _cbToCity.size() " + _cbToCity.getItemCount());
		//_cbToCity.setSelectedIndex(0);
	}
	
	public void removeToCityAll() {
		_cbToCity.removeAllItems();;
	}
	*/

	//public CBModelTo
	/**
	 * append intra team msg to the view;
	 * @param text the text to append
	 * @param label the sender
	 */
	public void appendIntraTeamMsg(String text, String label) {
		// TODO Auto-generated method stub
		System.out.println("[ClientGameView.appendIntraTeamMsg()] ... " + text + " from : " + label);
		StyledDocument document = textPane.getStyledDocument();

		Style style = textPane.addStyle("I'm a Style", null);
		StyleConstants.setForeground(style, Color.blue);
		try {
			document.insertString(document.getLength(), label + " : ", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		StyleConstants.setForeground(style, Color.black);
		try {
			document.insertString(document.getLength(), text + "\n", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * append inter team msg to the viwe
	 * @param text the text to append
	 * @param label the sender
	 */
	public void appendInterTeamMsg(String text, String label) {
		System.out.println("[ClientGameView.appendInterTeamMsg()] ... " + text + " from : " + label);
		StyledDocument document = textPane.getStyledDocument();
		Style style = textPane.addStyle("I'm a Style", null);
		StyleConstants.setForeground(style, Color.red);
		try {
			document.insertString(document.getLength(), label + " : ", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		StyleConstants.setForeground(style, Color.red);
		try {
			document.insertString(document.getLength(), text + "\n", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * end the sengoku game and show the winner.
	 * @param winner the winner
	 */
	public void endSengokuGame(int winner) {
		// disable buttons // show games; //
		_btnSubmitDecision.setEnabled(false);
		lblWinnerIs.setText("Team " + winner);
		lblWinnerIs.setBackground(Color.RED);
		appendInterTeamMsg("Congratinons! Team " + winner + " wins the game!", "**********");
	}

	/**
	 * update base soldier number
	 * @param num the number of soldier in the base
	 */
	public void updateBaseSoldierNumber(int num) {
		labelNumSoldierInBase.setText("" + num);
	}

	/**
	 * The selected base city
	 * @return the selected base city
	 */
	public CBType selectedFromCity() {
		return _cbFromCity.getItemAt(_cbFromCity.getSelectedIndex());

	}

	/**
	 * set the initial view (not actually in use, weird bugs)
	 */
	public void setInitialView() {

		/**
		while (_mapPanel == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("[ClientGameView.setInitialView()] waiting for _mapPanel to initialize");
		}
		_mapPanel.setPosition(Position.fromDegrees(40, -96, 6000e3), true);
		_mapPanel.repaint();
		*/
	}

}
