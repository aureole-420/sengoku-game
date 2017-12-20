package yt30_lp28.sengokugame.serverSide;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import provided.mixedData.MixedDataKey;
import yt30_lp28.sengokugame.clientSide.ClientGameController;

/**
 * Cmd for installing SegokuGame 
 *
 */
public class SengokuGameCmd extends DataPacketCRAlgoCmd<SengokuGameMsg> {

	private static final long serialVersionUID = 6848956386440650465L;
	/**
	 * Cmd 2 model Adapter
	 */
	private transient ICRCmd2ModelAdapter cmd2Model;
	/**
	 * receiver stub of game server;
	 */
	private IReceiver serverStub; //
	/**
	 * server uuid
	 */
	private UUID serverUUID;

	/**
	 * Constructor of sengoku game
	 * @param serverStub0 server's stub
	 * @param cmd2ModelAdapter cmd 2 model adapter
	 * @param serverUUID0 server's uuid
	 */
	public SengokuGameCmd(IReceiver serverStub0, ICRCmd2ModelAdapter cmd2ModelAdapter, UUID serverUUID0) {
		this.serverStub = serverStub0;
		this.cmd2Model = cmd2ModelAdapter;
		this.serverUUID = serverUUID0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(Class<?> index, DataPacketCR<SengokuGameMsg> msg, String... params) {
		Date time = new Date();
		String prefix;
		try {
			prefix = "\n[" + msg.getSender().getUserStub().getName() + "] <<GUESS_GAME_cmd>>  " + time + "\n";
			cmd2Model.appendMsg(prefix, null);
		} catch (RemoteException e) {
			System.err.println("<<<SengokuGameCmd.apply()>>> Exception calling remote method on IReceiver object "
					+ msg.getSender());
			e.printStackTrace();
		}
		// start game mvc
		ClientGameController game = new ClientGameController(serverStub, cmd2Model);
		game.start();

		// save cmd2gameAdapter
		ICmd2ClientGameAdapter clientGameAdapter = new ICmd2ClientGameAdapter() {
			@Override
			public void updateCityIconList(HashMap<String, CityIcon> remoteCityIcons) {
				game.getModel().updateCityIconList(remoteCityIcons);
			}

			@Override
			public void updateView() {
				game.getModel().updateView();
			}

			@Override
			public void setToCityCBinView() {
				game.getModel().setToCityCBinView();
			}

			@Override
			public void setFromCityCBinView(Collection<String> occupiedCities) {
				game.getModel().setFromCityCBinView(occupiedCities);
			}

			@Override
			public void appendIntraTeamMsg(String text, String label) {
				// TODO Auto-generated method stub
				game.getView().appendIntraTeamMsg(text, label);
			}

			@Override
			public void appendInterTeamMsg(String data, String name) {
				// TODO Auto-generated method stub
				game.getView().appendInterTeamMsg(data, name);
			}

			@Override
			public void endSengokuGame(int winner) {
				game.getView().endSengokuGame(winner);

			}

			@Override
			public void setAllAssignment(HashMap<String, Integer> allAssignment) {
				game.getModel().setAllAssignment(allAssignment);

			}

			@Override
			public void setInitialView() {
				game.getView().setInitialView();
			}
		};

		/*
		UUID serverUUID = null;
		try {serverUUID = msg.getSender().getUUID();} catch(RemoteException e1) {e1.printStackTrace();} 
		*/
		MixedDataKey<ICmd2ClientGameAdapter> key = new MixedDataKey<>(serverUUID, "SengokuGameAdapter",
				ICmd2ClientGameAdapter.class);
		cmd2Model.put(key, clientGameAdapter);
		System.out.println("<<SengokuGameCmd.apply()>> Stored ICmd2ClientGameAdapter with uuid:" + serverUUID);
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
