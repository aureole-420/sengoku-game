package common.datatype.user;

import common.IUser;
import common.IUserMessageType;

/**
 * Common data type for quitting.
 * When a user quits the app, the user will send such a data packet to all connected peers, so that 
 * they can remove the very user from their connected host list.
 */
public interface IQuitType extends IUserMessageType {

	/**
	 * Get the user stub who is quitting
	 * @return the quitting user
	 */
	public IUser getUserStub();
}
