package yt30_lp28.chatapp.impl;

import java.awt.Component;
import java.rmi.RemoteException;

import common.DataPacketCRAlgoCmd;
import common.DataPacketCR;
import common.ICRCmd2ModelAdapter;
import common.IComponentFactory;

/**
 * CompCmd is a command for processing Component data type
 */
public class CompCmd extends DataPacketCRAlgoCmd<MyComponent> {
	private static final long serialVersionUID = 1993837305646164419L;
	private transient ICRCmd2ModelAdapter cmd2ModelAdpt;

	/**
	 * Constructor for CompCmd
	 * @param cmd2ModelAdpt ICmd2ModelAdapter for the command talking to local system
	 */
	public CompCmd(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<MyComponent> host, String... params) {
		String name = "Unknown remote name ";
		try {
			name = host.getSender().getUserStub().getName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		cmd2ModelAdpt.buildNonScrollableComponent(new IComponentFactory() {
			@Override
			public Component makeComponent() {
				return new MyPanel();
			}
		}, name);
		cmd2ModelAdpt.appendMsg("[CompCmd ] .... successfully installed a comp", name);
		return null;
	}
}
