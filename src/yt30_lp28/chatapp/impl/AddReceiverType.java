package yt30_lp28.chatapp.impl;

import common.IReceiver;
import common.datatype.chatroom.IAddReceiverType;

/**
 * Concrete class implementing IAddReceiverType for transmitting message to add receiver
 */
public class AddReceiverType implements IAddReceiverType {

	private static final long serialVersionUID = -6657554451862486319L;
	private IReceiver receiverStub;

	/**
	 * Constructor for AddReceiverType
	 * @param receiverStub the receiver stub to add
	 */
	public AddReceiverType(IReceiver receiverStub) {
		this.receiverStub = receiverStub;
	}

	@Override
	public IReceiver getReceiverStub() {
		return receiverStub;
	}
}
