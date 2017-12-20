package yt30_lp28.sengokugame.serverSide;

import java.rmi.RemoteException;
import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import provided.mixedData.MixedDataKey;

/**
 * The cmd for interteamStringMsg
 */
public class InterTeamStringCmd extends DataPacketCRAlgoCmd<InterTeamStringMsg> {

	private static final long serialVersionUID = -3065157534766693322L;
	/**
	 * Cmd to Model Adapter
	 */
	private transient ICRCmd2ModelAdapter cmd2ModelAdpt;
	/**
	 * Server's UUID
	 */
	private UUID serverUUID;

	/**
	 * Constructor for this cmd
	 * @param cmd2ModelAdpt the cmd to model adapter 
	 * @param serverUUID the server's UUID
	 */
	public InterTeamStringCmd(ICRCmd2ModelAdapter cmd2ModelAdpt, UUID serverUUID) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
		this.serverUUID = serverUUID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(Class<?> index, DataPacketCR<InterTeamStringMsg> host, String... params) {
		MixedDataKey<ICmd2ClientGameAdapter> key = new MixedDataKey<>(serverUUID, "SengokuGameAdapter",
				ICmd2ClientGameAdapter.class);
		ICmd2ClientGameAdapter gameAdapter = cmd2ModelAdpt.get(key);
		if (gameAdapter == null) {
			System.out.println("Game not installed ... ");
			return null;
		}
		try {
			gameAdapter.appendInterTeamMsg(host.getData().getData(), host.getSender().getUserStub().getName());
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
