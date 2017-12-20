package yt30_lp28.server.mvc;

import java.util.List;
import common.IChatRoom;
import yt30_lp28.chatapp.impl.IUser2ModelAdapter;

/**
 * Adapter for the user talking with the model
 */
public interface IServerUser2ModelAdapter extends IUser2ModelAdapter {

	/**
	 * Get the lobby chat room
	 * @return the lobby
	 */
	public IChatRoom getLobbyRoom();

	/**
	 * Get all teams in the model
	 * @return team list
	 */
	public List<IChatRoom> getTeams();
}
