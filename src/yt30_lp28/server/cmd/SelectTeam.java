package yt30_lp28.server.cmd;

import common.ICRMessageType;
import common.IChatRoom;
import common.IUserMessageType;

/**
 * Message type for sending selected team back to the game server, can be used in both user and chat room level
 */
public class SelectTeam implements ICRMessageType, IUserMessageType {
	private static final long serialVersionUID = -350114409519654722L;
	private IChatRoom room;

	/**
	 * Constructor for SelectTeam, takes the selected chat room as the parameter
	 * @param room
	 */
	public SelectTeam(IChatRoom room) {
		this.room = room;
	}

	/**
	 * Get the selected chat room
	 * @return the selected team
	 */
	public IChatRoom getRoom() {
		return room;
	}

}
