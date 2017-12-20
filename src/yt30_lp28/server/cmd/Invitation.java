package yt30_lp28.server.cmd;

import common.IChatRoom;
import common.datatype.user.IInvitationType;

/**
 * Invitation type
 */
public class Invitation implements IInvitationType {
	private static final long serialVersionUID = 2828602133855525005L;
	private IChatRoom chatRoom;

	/**
	 * Constructor for Invitation
	 * @param chatRoom the chat room in the invitation
	 */
	public Invitation(IChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	@Override
	public IChatRoom getChatRoom() {
		return chatRoom;
	}

}
