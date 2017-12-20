package yt30_lp28.sengokugame.serverSide;

import common.ICRMessageType;

/**
 * The intra team msg for msg transmission between teams;
 */
public class IntraTeamStringMsg implements ICRMessageType {
	private static final long serialVersionUID = 5065116501162155560L;
	/**
	 * The msg to trasnmit
	 */
	private String data;

	/**
	 * Constructor of this msg type;
	 * @param data the msg to transmit
	 */
	public IntraTeamStringMsg(String data) {
		this.data = data;
	}

	/**
	 * getter for msg
	 * @return the msg
	 */
	public String getData() {
		return this.data;
	}
}
