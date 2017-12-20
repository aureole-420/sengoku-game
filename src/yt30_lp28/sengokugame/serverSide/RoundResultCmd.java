package yt30_lp28.sengokugame.serverSide;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import provided.mixedData.MixedDataKey;

/**
 * Cmd for Round Result Msg
 */
public class RoundResultCmd extends DataPacketCRAlgoCmd<RoundResultMsg> {

	private static final long serialVersionUID = -4544145214402859350L;
	/**
	 * Cmd 2 model adapter
	 */
	private transient ICRCmd2ModelAdapter cmd2Model;
	/**
	 * Server's uuid;
	 */
	private UUID serverUUID;

	/**
	 * Constructor of the round result msg;
	 * @param cmd2Model0 the cmd 2 model adapter
	 * @param serverUUID server's uuid
	 */
	public RoundResultCmd(ICRCmd2ModelAdapter cmd2Model0, UUID serverUUID) {
		this.cmd2Model = cmd2Model0;
		this.serverUUID = serverUUID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(Class<?> index, DataPacketCR<RoundResultMsg> msg, String... params) {
		// getting data 
		HashMap<String, CityIcon> remoteCityIcons = msg.getData().getCityIcons();
		Collection<String> occupiedCities = msg.getData().getAllAssignment().keySet();
		HashMap<String, Integer> allAssignment = msg.getData().getAllAssignment();

		// retrieve game adapter
		/*
		UUID serverUUID = null;
		try {serverUUID = msg.getSender().getUUID();} 
		catch (RemoteException e) {
			System.out.println("<<RoundResultCmd.apply()>> remote exception getting uuid of game server.");
			e.printStackTrace();
		}
		*/

		MixedDataKey<ICmd2ClientGameAdapter> key = new MixedDataKey<>(serverUUID, "SengokuGameAdapter",
				ICmd2ClientGameAdapter.class);
		while (cmd2Model.get(key) == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("RoundResultCmd.apply() Waiting for the game to install...");
		}
		ICmd2ClientGameAdapter gameAdapter = cmd2Model.get(key);
		gameAdapter.setInitialView();
		gameAdapter.updateCityIconList(remoteCityIcons);
		gameAdapter.updateView();
		gameAdapter.setAllAssignment(allAssignment);
		gameAdapter.setFromCityCBinView(occupiedCities);
		gameAdapter.setToCityCBinView();

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
