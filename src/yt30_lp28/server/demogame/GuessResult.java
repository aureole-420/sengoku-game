package yt30_lp28.server.demogame;

import common.ICRMessageType;
import common.IUserMessageType;

/**
 *
 */
public class GuessResult implements IUserMessageType, ICRMessageType {
	private static final long serialVersionUID = 1773177368843144357L;
	private String result;

	/**
	 * @param result
	 */
	public GuessResult(String result) {
		this.setResult(result);
	}

	/**
	 * Get the result string
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Set the result string
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}
}
