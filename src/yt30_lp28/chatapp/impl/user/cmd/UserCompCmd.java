package yt30_lp28.chatapp.impl.user.cmd;

import java.awt.Component;
import java.rmi.RemoteException;

import common.DataPacketUser;
import common.DataPacketUserAlgoCmd;
import common.IComponentFactory;
import common.IUserCmd2ModelAdapter;
import yt30_lp28.chatapp.impl.MyComponent;
import yt30_lp28.chatapp.impl.MyPanel;

/**
 * Command for sending component in user level.
 */
public class UserCompCmd extends DataPacketUserAlgoCmd<MyComponent> {
	private static final long serialVersionUID = -535364001229524867L;
	private transient IUserCmd2ModelAdapter cmd2ModelAdpt;

	/**
	 * Constructor for UserCompCmd, takes a IUserCmd2ModelAdapter as the parameter
	 * @param cmd2ModelAdpt the IUserCmd2ModellAdapter object for command talking with local system
	 */
	public UserCompCmd(IUserCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

	@Override
	public String apply(Class<?> index, DataPacketUser<MyComponent> host, String... params) {
		String name = "Unknown remote name ";
		try {
			name = host.getSender().getName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		cmd2ModelAdpt.buildNonScrollableComponent(new IComponentFactory() {
			@Override
			public Component makeComponent() {
				return new MyPanel();
			}
		}, name);
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(IUserCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
}
