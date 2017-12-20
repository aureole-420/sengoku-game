package common.datatype.user;

import common.IChatRoom;
import common.IUserMessageType;

/**
 * Well known data type to invite a user to a chat room
 */
public interface IInvitationType extends IUserMessageType {

	/**
	 * Get the chat room
	 * @return the chat room in the invitation
	 */
	public IChatRoom getChatRoom();
}
