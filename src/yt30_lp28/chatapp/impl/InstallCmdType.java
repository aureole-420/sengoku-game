package yt30_lp28.chatapp.impl;

import common.DataPacketCRAlgoCmd;
import common.datatype.chatroom.ICRInstallCmdType;

/**
 * Implementation of IInstallCmdType for sending packet to install command
 */
public class InstallCmdType implements ICRInstallCmdType {

	private static final long serialVersionUID = 1797129479938550778L;
	private DataPacketCRAlgoCmd<?> cmd;
	private Class<?> cmdId;

	/**
	 * Constructor for InstallCmdType
	 * @param cmdId index of the command
	 * @param cmd the command to install
	 */
	public InstallCmdType(Class<?> cmdId, DataPacketCRAlgoCmd<?> cmd) {
		this.cmd = cmd;
		this.cmdId = cmdId;
	}

	@Override
	public DataPacketCRAlgoCmd<?> getCmd() {
		return cmd;
	}

	@Override
	public Class<?> getCmdId() {
		return cmdId;
	}
}
