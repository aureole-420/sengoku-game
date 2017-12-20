package yt30_lp28.sengokugame.serverSide;

import common.ICRMessageType;

/**
 * The message type for the interteam message passing.
 * @author lp28, yt30
 */
public class InterTeamStringMsg implements ICRMessageType {

	private static final long serialVersionUID = 265295373801177148L;
	/**
	 * The message to be transmitted
	 */
	private String data;

	/**
	 * Constructor of inter team string msg type
	 * @param data the msg
	 */
	public InterTeamStringMsg(String data) {
		this.data = data;
	}

	/**
	 * getter for msg;
	 * @return the msg;
	 */
	public String getData() {
		return this.data;
	}
}
