package yt30_lp28.chatapp.impl;

import common.IChatRoom;
import common.IUser;
import common.IUserCmd2ModelAdapter;

/**
 * Adapter for the user talking with model/view, to support auto-connect-back feature
 */
public interface IUser2ModelAdapter extends IUserCmd2ModelAdapter {

	/**
	 * Add a connected user stub in the model/view
	 * @param userStub the user stub to add
	 */
	public void addConnected(IUser userStub);

	/**
	 * Join a chat room
	 * @param room the chat room to join
	 */
	public void joinChatRoom(IChatRoom room);

	/**
	 * Remove a connected user stub in the model/view
	 * @param userStub the user stub to remove
	 */
	public void removeConnected(IUser userStub);
}
