package yt30_lp28.sengokugame.serverSide;

import java.rmi.RemoteException;
import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import provided.mixedData.MixedDataKey;

/**
 * The Cmd for SengokuGameEngMsg
 */
public class SengokuGameEndCmd extends DataPacketCRAlgoCmd<SengokuGameEndMsg> {

	private static final long serialVersionUID = -1883338385400059760L;
	/**
	 * Cmd 2 model Adapter
	 */
	private transient ICRCmd2ModelAdapter cmd2Model;
	/**
	 * Server's uuid;
	 */
	private UUID serverUUID;

	/**
	 * Constructor of SengokuGameEndCmd
	 * @param cmd2Model the cmd 2 model adapter
	 * @param serverUUID0 server's uuid;
	 */
	public SengokuGameEndCmd(ICRCmd2ModelAdapter cmd2Model, UUID serverUUID0) {
		super();
		this.cmd2Model = cmd2Model;
		this.serverUUID = serverUUID0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(Class<?> index, DataPacketCR<SengokuGameEndMsg> msg, String... params) {
		int winner = msg.getData().getWinner();
		if (serverUUID != null) {
			MixedDataKey<ICmd2ClientGameAdapter> key = new MixedDataKey<>(serverUUID, "SengokuGameAdapter",
					ICmd2ClientGameAdapter.class);
			while (cmd2Model.get(key) == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ICmd2ClientGameAdapter gameAdapter = cmd2Model.get(key);
			gameAdapter.endSengokuGame(winner);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2Model = cmd2ModelAdpt;
	}

}
