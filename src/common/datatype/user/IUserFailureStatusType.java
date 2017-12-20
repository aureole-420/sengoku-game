package common.datatype.user;

import common.DataPacketUser;
import common.IUserMessageType;

/**
 * Well known data type for sending failure status
 */
public interface IUserFailureStatusType extends IUserMessageType {
	/**
	 * The corresponding original data for the failure status
	 * @return the original data
	 */
	public DataPacketUser<? extends IUserMessageType> getOriginalData();

	/**
	 * The detailed failure information
	 * @return the detailed failure info
	 */
	public String getFailureInfo();
}
