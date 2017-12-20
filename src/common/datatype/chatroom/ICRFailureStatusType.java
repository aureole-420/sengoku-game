package common.datatype.chatroom;

import common.DataPacketCR;
import common.ICRMessageType;

/**
 * Well known data type for sending failure status
 */
public interface ICRFailureStatusType extends ICRMessageType {
	/**
	 * The corresponding original data for the failure status
	 * @return the original data
	 */
	public DataPacketCR<? extends ICRMessageType> getOriginalData();

	/**
	 * The detailed failure information
	 * @return the detailed failure info
	 */
	public String getFailureInfo();
}
