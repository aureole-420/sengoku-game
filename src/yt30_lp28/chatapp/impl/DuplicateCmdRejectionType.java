package yt30_lp28.chatapp.impl;

import common.DataPacketCR;
import common.datatype.chatroom.ICRRejectionStatusType;

/**
 * DuplicateCmdRejectionType defines a failure because of duplicate commands.
 * When a remote user returns a command (in IInstallCmdType data packet) 
 * which local system already installs, local system returns such a failure.
 */
public class DuplicateCmdRejectionType implements ICRRejectionStatusType {

	private static final long serialVersionUID = -4348638476517359246L;
	private DataPacketCR<?> originalData;
	private Class<?> cmdId;

	/**
	 * Constructor for DuplicateCmdRejectionType
	 * @param originalData the original data packet causing the rejection
	 * @param cmdId index of the command which is duplicate
	 */
	public DuplicateCmdRejectionType(DataPacketCR<?> originalData, Class<?> cmdId) {
		this.originalData = originalData;
		this.cmdId = cmdId;
	}

	@Override
	public DataPacketCR<?> getOriginalData() {
		return originalData;
	}

	@Override
	public String getFailureInfo() {
		return "Command already installed for id " + cmdId;
	}

}
