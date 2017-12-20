package yt30_lp28.chatapp.impl;

import common.DataPacketCR;
import common.datatype.chatroom.ICRExceptionStatusType;

/**
 * CmdNotFoundExceptionType defines an exception because cannot find the required command.
 * When a remote user requests a command which the local system don't have actually,
 * local system returns such an exception.  
 */
public class CmdNotFoundExceptionType implements ICRExceptionStatusType {

	private static final long serialVersionUID = -8821153344543680405L;
	private DataPacketCR<?> originalData;
	private Class<?> cmdId;

	/**
	 * Constructor for CmdNotFoundExceptionType
	 * @param originalData the original data packet causing the exception
	 * @param cmdId  index of the command
	 */
	public CmdNotFoundExceptionType(DataPacketCR<?> originalData, Class<?> cmdId) {
		this.originalData = originalData;
		this.cmdId = cmdId;
	}

	@Override
	public DataPacketCR<?> getOriginalData() {
		return originalData;
	}

	@Override
	public String getFailureInfo() {
		return "Command not found for id " + cmdId;
	}
}
