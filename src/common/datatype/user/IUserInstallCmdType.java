package common.datatype.user;

import common.DataPacketUserAlgoCmd;
import common.IUserMessageType;

/**
 * Well known data type for transmitting command between users.
 * Specify the command type to be DataPacketUserAlgoCmd
 */
public interface IUserInstallCmdType extends IUserMessageType {
	/**
	 *  Get the command
	 * @return the command
	 */
	public DataPacketUserAlgoCmd<?> getCmd();

	/**
	 * Get the command id
	 * @return the command id
	 */
	public Class<?> getCmdId();
}