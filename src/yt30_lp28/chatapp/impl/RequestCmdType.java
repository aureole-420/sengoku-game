package yt30_lp28.chatapp.impl;

import common.datatype.IRequestCmdType;

/**
 * Implementation of IRequestCmdType for sending packet to request command
 */
public class RequestCmdType implements IRequestCmdType {
	private static final long serialVersionUID = -871772480618482857L;
	private Class<?> cmdId;

	/**
	 * Constructor for RequestCmdType
	 * @param cmdId index of the command
	 */
	public RequestCmdType(Class<?> cmdId) {
		this.cmdId = cmdId;
	}

	@Override
	public Class<?> getCmdId() {
		return cmdId;
	}
}
