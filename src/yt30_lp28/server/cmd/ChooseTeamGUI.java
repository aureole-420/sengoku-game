package yt30_lp28.server.cmd;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import common.ICRCmd2ModelAdapter;
import common.IChatRoom;
import common.IReceiver;

import javax.swing.JLabel;

/**
 * Choose team GUI, sending from the serve to the client for choosing teams
 */
public class ChooseTeamGUI extends JPanel {

	private static final long serialVersionUID = -4203191507259007194L;
	private final JPanel panel = new JPanel();
	private final JPanel panel_1 = new JPanel();
	private final JComboBox<IChatRoom> comboBox = new JComboBox<IChatRoom>();
	private final JButton btnNewButton = new JButton("Submit");
	private final JTextField textField = new JTextField();
	private final JButton btnNewButton_1 = new JButton("Submit");
	private final JLabel lblSelectTeam = new JLabel("Select Team");
	private final JLabel lblCreateTeam = new JLabel("Create Team");
	private ICRCmd2ModelAdapter cmd2ModelAdpt;
	private IReceiver receiverStub;

	/**
	 * Create the panel.
	 * @param items 
	 * @param cmd2ModelAdpt 
	 * @param receiverStub 
	 */
	public ChooseTeamGUI(Iterable<IChatRoom> items, ICRCmd2ModelAdapter cmd2ModelAdpt, IReceiver receiverStub) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
		this.receiverStub = receiverStub;
		initGUI();
		for (IChatRoom t : items)
			comboBox.insertItemAt(t, 0);
	}

	private void initGUI() {
		textField.setColumns(10);
		setLayout(new BorderLayout(0, 0));
		add(panel, BorderLayout.NORTH);
		panel.add(lblSelectTeam);
		panel.add(comboBox);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmd2ModelAdpt.sendTo(receiverStub, SelectTeam.class,
						new SelectTeam(comboBox.getItemAt(comboBox.getSelectedIndex())));
			}
		});
		panel.add(btnNewButton);

		add(panel_1, BorderLayout.CENTER);

		panel_1.add(lblCreateTeam);

		panel_1.add(textField);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Click button -------");
			}
		});

		panel_1.add(btnNewButton_1);
	}
}