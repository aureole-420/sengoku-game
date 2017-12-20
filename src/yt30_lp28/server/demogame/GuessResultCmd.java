package yt30_lp28.server.demogame;

import java.rmi.RemoteException;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import provided.mixedData.MixedDataKey;

/**
 *
 */
public class GuessResultCmd extends DataPacketCRAlgoCmd<GuessResult> {
	private static final long serialVersionUID = 6762125727299839817L;
	private transient ICRCmd2ModelAdapter cmd2ModelAdpt;
	private IReceiver receiverStub;

	/**
	 * @param receiverStub
	 */
	public GuessResultCmd(IReceiver receiverStub) {
		this.receiverStub = receiverStub;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<GuessResult> host, String... params) {
		try {
			MixedDataKey<IServer2GameAdapter> key = new MixedDataKey<>(receiverStub.getUUID(), "IServer2GameAdapter",
					IServer2GameAdapter.class);
			System.out.println("<<GuessResultCmd.apply()>> cmd2ModelAdpt == null" + (cmd2ModelAdpt == null));
			IServer2GameAdapter adpt = cmd2ModelAdpt.get(key);
			if (adpt == null) {
				System.out.println("Error: IServer2GameAdapter == null");
			} else {
				adpt.showResult(host.getData().getResult());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		// TODO Auto-generated method stub
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}
