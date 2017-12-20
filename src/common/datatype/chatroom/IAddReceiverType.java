package common.datatype.chatroom;

import common.ICRMessageType;
import common.IReceiver;

/**
 * Well known data type for adding receiver.
 * We explicitly carry the IReceiver to add instead of relying on the assumption that 
 * that the IReceiver to add is the sender, to give greater flexibility to the system
 */
public interface IAddReceiverType extends ICRMessageType {

	/**
	 * Get the receiver stub to add
	 * @return the receiver stub to add
	 */
	public IReceiver getReceiverStub();
}
