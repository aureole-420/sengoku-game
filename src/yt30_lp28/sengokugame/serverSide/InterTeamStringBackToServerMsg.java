package yt30_lp28.sengokugame.serverSide;

import common.ICRMessageType;

/**
 * The  msg-type for Interteam msg transmission from the game client to another VIA the server
 */
public class InterTeamStringBackToServerMsg implements ICRMessageType {

	private static final long serialVersionUID = 1052342280475119496L;
	/**
	 * The message to send
	 */
	private String data;

	/**
	 * The constructor for this msg.
	 * @param data the message to send
	 */
	public InterTeamStringBackToServerMsg(String data) {
		this.data = data;
	}

	/**
	 * getter for the msg
	 * @return the msg
	 */
	public String getData() {
		return data;
	}

	/**
	 * setter for the msg
	 * @param data the msg
	 */
	public void setData(String data) {
		this.data = data;
	}
}
