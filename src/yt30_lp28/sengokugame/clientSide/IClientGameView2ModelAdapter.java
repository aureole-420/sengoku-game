package yt30_lp28.sengokugame.clientSide;

import common.ICRMessageType;
import yt30_lp28.chatapp.impl.MyString;
import yt30_lp28.sengokugame.serverSide.CityIcon;
import yt30_lp28.sengokugame.serverSide.InterTeamStringMsg;

/**
 * The adapter allows calling game model's method
 * @author yt30, lp28
 *
 * @param <CBType> The data type for the combo box in view;
 */
public interface IClientGameView2ModelAdapter<CBType> {

	/**
	 * send decision to server;
	 * @param cityFrom the base city
	 * @param cityTo the target city
	 * @param noCtz2Move the number of soldiers to send 
	 */
	public void sendDecision(Object cityFrom, Object cityTo, int noCtz2Move);

	/**
	 * Send messages within Team
	 * @param id index of msg type
	 * @param data the msg
	 */
	public <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data);

	/**
	 * send msg among teams;
	 * @param id index of msg type
	 * @param data the msg
	 */
	public <T extends ICRMessageType> void sendInterTeamMsg(Class<T> id, T data);

	/**
	 * Getter for the number of soldiers in base city
	 * @param city the base city
	 * @return the number of soldiers in base city
	 */
	public int getNumSoldiersInBase(CBType city);

	/**
	 * Update Range Layer for base city
	 * @param baseCity the base city;
	 */
	public void updateRangeLayer(CBType baseCity);

}
