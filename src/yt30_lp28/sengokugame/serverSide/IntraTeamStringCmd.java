package yt30_lp28.sengokugame.serverSide;

import java.rmi.RemoteException;
import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import provided.mixedData.MixedDataKey;

/**
 * The cmd for IntraTeamMsg
 */
public class IntraTeamStringCmd extends DataPacketCRAlgoCmd<IntraTeamStringMsg> {

	private static final long serialVersionUID = -3514914667689273673L;

	/**
	 * The cmd2ModelAdapter
	 */
	private transient ICRCmd2ModelAdapter cmd2ModelAdpt;
	/**
	 * server's UUID;
	 */
	private UUID serverUUID;

	/**
	 * The constructor for the intrateam string cmd 
	 * @param cmd2ModelAdpt cmd to model adapter
	 * @param serverUUID server's UUID
	 */
	public IntraTeamStringCmd(ICRCmd2ModelAdapter cmd2ModelAdpt, UUID serverUUID) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
		this.serverUUID = serverUUID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(Class<?> index, DataPacketCR<IntraTeamStringMsg> host, String... params) {
		MixedDataKey<ICmd2ClientGameAdapter> key = new MixedDataKey<>(serverUUID, "SengokuGameAdapter",
				ICmd2ClientGameAdapter.class);
		ICmd2ClientGameAdapter gameAdapter = cmd2ModelAdpt.get(key);
		if (gameAdapter == null) {
			System.out.println("Game not installed ... ");
			return null;
		}
		try {
			gameAdapter.appendIntraTeamMsg(host.getData().getData(), host.getSender().getUserStub().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}
