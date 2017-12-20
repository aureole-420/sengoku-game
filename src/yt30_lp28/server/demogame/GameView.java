package yt30_lp28.server.demogame;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

/**
 *
 */
public class GameView extends JFrame {

	private static final long serialVersionUID = 4949625217809968241L;
	private JPanel contentPane;
	private final JPanel panel = new JPanel();
	private final JTextField textField = new JTextField();
	private final JButton btnGuess = new JButton("Guess");
	private final JPanel panel_1 = new JPanel();
	private final JTextArea txtrResults = new JTextArea();
	private IView2ModelAdapter view2ModelAdpt;
	private final JLabel lblNewLabel = new JLabel("Guess a number between 0 - 100");

	//	/**
	//	 * Launch the application.
	//	 */
	//	public static void main(String[] args) {
	//		EventQueue.invokeLater(new Runnable() {
	//			public void run() {
	//				try {
	//					GameView frame = new GameView();
	//					frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//	}

	/**
	 * Create the frame.
	 * @param view2ModelAdpt 
	 */
	public GameView(IView2ModelAdapter view2ModelAdpt) {
		this.view2ModelAdpt = view2ModelAdpt;
		initGUI();
	}

	/**
	 * 
	 */
	public void start() {
		this.setVisible(true);
	}

	private void initGUI() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane
				.setBorder(new TitledBorder(null, "Guess Number", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(panel, BorderLayout.NORTH);

		panel.add(lblNewLabel);
		textField.setColumns(10);
		panel.add(textField);
		btnGuess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Game View ----" + textField.getText());
				showResult("You guessed : " + textField.getText());
				view2ModelAdpt.guess(Integer.valueOf(textField.getText()));
			}
		});

		panel.add(btnGuess);

		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		txtrResults.setText("Results\n");

		panel_1.add(txtrResults);
	}

	/**
	 * @param text
	 */
	public void setLabel(String text) {
		lblNewLabel.setText(text);
	}

	/**
	 * @param t
	 */
	public void showResult(String t) {
		System.out.println(t);
		txtrResults.append(t + "\n");
	}
}
