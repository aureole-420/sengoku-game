package yt30_lp28.chatapp.impl;

import java.rmi.RemoteException;

import common.DataPacketCRAlgoCmd;
import common.DataPacketCR;
import common.ICRCmd2ModelAdapter;

/**
 * StringCmd is a command for processing String data packet
 */
public class StringCmd extends DataPacketCRAlgoCmd<MyString> {
	private static final long serialVersionUID = -785454239038688327L;
	private transient ICRCmd2ModelAdapter cmd2ModelAdpt;

	/**
	 * Constructor for StringCmd
	 * @param cmd2ModelAdpt adapter for the command talking with local system
	 */
	public StringCmd(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<MyString> host, String... params) {
		String name = "Unknown remote name ";
		try {
			name = host.getSender().getUserStub().getName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		cmd2ModelAdpt.appendMsg(host.getData().getData(), name);
		return null;
	}

}
