package yt30_lp28.chatapp.impl.user.cmd;

import java.rmi.RemoteException;

import common.DataPacketUser;
import common.DataPacketUserAlgoCmd;
import common.IUserCmd2ModelAdapter;
import yt30_lp28.chatapp.impl.MyString;

/**
 * Command for sending String message in user level
 */
public class UserStringCmd extends DataPacketUserAlgoCmd<MyString> {

	private transient IUserCmd2ModelAdapter cmd2ModelAdpt;
	private static final long serialVersionUID = -271453217489602349L;

	/**
	 * Constructor for UserCompCmd, takes a IUserCmd2ModelAdapter as the parameter
	 * @param cmd2ModelAdpt the IUserCmd2ModellAdapter object for command talking with local system
	 */
	public UserStringCmd(IUserCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

	@Override
	public String apply(Class<?> index, DataPacketUser<MyString> host, String... params) {
		// TODO Auto-generated method stub
		String name = "Unknown remote name ";
		try {
			name = host.getSender().getName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		cmd2ModelAdpt.appendMsg(host.getData().getData(), name);
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(IUserCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
}
