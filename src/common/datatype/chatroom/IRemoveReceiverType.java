package common.datatype.chatroom;

import common.ICRMessageType;
import common.IReceiver;

/**
 * Well known data type for removing receiver.
 * We explicitly carry the IReceiver to remove instead of relying on the assumption that 
 * that the IReceiver to remove is the sender, to give greater flexibility to the system
 */
public interface IRemoveReceiverType extends ICRMessageType {

	/**
	 * Get the receiver stub to remove
	 * @return the receiver stub to remove
	 */
	public IReceiver getReceiverStub();

}