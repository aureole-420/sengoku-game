package yt30_lp28.chatapp.impl.user.cmd;

import common.DataPacketUserAlgoCmd;
import common.datatype.user.IUserInstallCmdType;

/**
 * Common message type command implementation.
 * Message type for auto installing command in user level communication
 */
public class UserInstallCmdType implements IUserInstallCmdType {
	private static final long serialVersionUID = -8163853442189683562L;
	private DataPacketUserAlgoCmd<?> cmd;
	private Class<?> cmdId;

	/**
	 * Constructor for UserInstallCmdType, takes the command id and command as parameters
	 * @param cmdId the command id
	 * @param cmd the command
	 */
	public UserInstallCmdType(Class<?> cmdId, DataPacketUserAlgoCmd<?> cmd) {
		this.cmd = cmd;
		this.cmdId = cmdId;
	}

	@Override
	public DataPacketUserAlgoCmd<?> getCmd() {
		return cmd;
	}

	@Override
	public Class<?> getCmdId() {
		return cmdId;
	}
}
