package yt30_lp28.server.impl;

import java.util.List;

import common.IChatRoom;
import common.IUser;
import yt30_lp28.chatapp.impl.ILocalCmd2ModelAdapter;
import yt30_lp28.sengokugame.serverSide.ServerGameModel;

/**
 * IServerCRCmd2ModelAdapter
 */
public interface IServerCRCmd2ModelAdapter extends ILocalCmd2ModelAdapter {
	/**
	 * A list of chat rooms
	 * @return a list of chat rooms
	 */
	public List<IChatRoom> getChatRooms();

	/**
	 * Invite a remote user to a chat room
	 * @param invitedUser the invited user
	 * @param theRoomToJoin the invited chat room 
	 */
	public void inviteToChatRoom(IUser invitedUser, IChatRoom theRoomToJoin);

	/**
	 * Get the SengokuGameModel object
	 * @return the SengokuGameModel object
	 */
	public ServerGameModel getSengokuGameModel();
}