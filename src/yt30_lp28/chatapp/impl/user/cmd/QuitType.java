package yt30_lp28.chatapp.impl.user.cmd;

import common.IUser;
import common.datatype.user.IQuitType;

/**
 * Implementing IQuitType interface
 */
public class QuitType implements IQuitType {

	private static final long serialVersionUID = 4245466765864607691L;
	private IUser userStub;

	/**
	 * Constructor for QuitType
	 * @param userStub the quitting user stub
	 */
	public QuitType(IUser userStub) {
		this.userStub = userStub;
	}

	@Override
	public IUser getUserStub() {
		// TODO Auto-generated method stub
		return userStub;
	}
}