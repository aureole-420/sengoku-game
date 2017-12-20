package yt30_lp28.server.demogame;

import common.ICRMessageType;
import common.IUserMessageType;

/**
 * Message type for installing the game, the corresponding command is DemoGameCmd
 */
public class DemoGameMsg implements IUserMessageType, ICRMessageType {
	private static final long serialVersionUID = -1756279039460680790L;

	/**
	 * Constructor for GameMsg
	 */
	public DemoGameMsg() {
	}
}
