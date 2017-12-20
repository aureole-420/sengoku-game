package yt30_lp28.sengokugame.serverSide;

import common.ICRMessageType;

/**
 * Message Type that notifies all users a game has ended;
 * @author yt30, lp28
 */
public class SengokuGameEndMsg implements ICRMessageType {

	private static final long serialVersionUID = -3181158224771082649L;
	/**
	 * The winner of the game
	 */
	private int winner; // the number of winning team;

	/**
	 * Constructor of this msg.
	 * @param winner	 The winner of the game
	 */
	public SengokuGameEndMsg(int winner) {
		super();
		this.winner = winner;
	}

	/**
	 * Getter winner;
	 * @return the winner;
	 */
	public int getWinner() {
		return winner;
	}
}
