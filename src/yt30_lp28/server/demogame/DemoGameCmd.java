package yt30_lp28.server.demogame;

import javax.swing.SwingUtilities;

import common.DataPacketCRAlgoCmd;
import common.DataPacketCR;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;

/**
 * Command for installing the demo game, the corresponding message type is DemoGameMsg
 */
public class DemoGameCmd extends DataPacketCRAlgoCmd<DemoGameMsg> {
	private static final long serialVersionUID = -7010990632501348552L;
	private transient ICRCmd2ModelAdapter cmd2ModelAdpt;
	private IReceiver receiverStub;

	/**
	 * @param cmd2ModelAdpt
	 * @param receiverStub
	 */
	public DemoGameCmd(ICRCmd2ModelAdapter cmd2ModelAdpt, IReceiver receiverStub) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
		this.receiverStub = receiverStub;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<DemoGameMsg> host, String... params) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					GameController game = new GameController(receiverStub, cmd2ModelAdpt);
					game.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
}
