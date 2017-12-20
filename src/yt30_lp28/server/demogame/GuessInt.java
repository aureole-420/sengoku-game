package yt30_lp28.server.demogame;

import common.ICRMessageType;
import common.IUserMessageType;

/**
 * Message type for sending guess number back to server.
 */
public class GuessInt implements IUserMessageType, ICRMessageType {
	private static final long serialVersionUID = 5735663118135418609L;
	private int guess;

	/**
	 * Constructor for GuessInt, takes the guess number as the parameter
	 * @param guess the guess number
	 */
	public GuessInt(int guess) {
		this.guess = guess;
	}

	/**
	 * Get the guess number
	 * @return the guess number
	 */
	public int getGuess() {
		return guess;
	}
}
