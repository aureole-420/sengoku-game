package common.datatype.chatroom;

import common.DataPacketCRAlgoCmd;
import common.ICRMessageType;

/**
 * Well known data type for installing command in a chat room.
 * Specify the command type to be DataPacketCRAlgoCmd
 */
public interface ICRInstallCmdType extends ICRMessageType {

	/**
	 *  Get the command
	 * @return the command
	 */
	public DataPacketCRAlgoCmd<?> getCmd();

	/**
	 * Get the command id
	 * @return the command id
	 */
	public Class<?> getCmdId();
}
