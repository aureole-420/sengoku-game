package yt30_lp28.chatapp.impl;

import java.awt.Component;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import common.DataPacketCRAlgoCmd;
import common.DataPacketCR;
import common.ICRCmd2ModelAdapter;
import common.IComponentFactory;

/**
 * ImageCmd is a command for processing ImageIcon data packet
 */
public class ImageCmd extends DataPacketCRAlgoCmd<MyImageIcon> {
	private static final long serialVersionUID = 7038518219319273106L;
	private transient ICRCmd2ModelAdapter cmd2ModelAdpt;

	/**
	 * Constructor for ImageCmd
	 * @param cmd2ModelAdpt ICmd2ModelAdapter for the command talking to local system
	 */
	public ImageCmd(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<MyImageIcon> host, String... params) {
		String name = "Unknown remote name ";
		try {
			name = host.getSender().getUserStub().getName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		ImageIcon img = host.getData().getImage();
		cmd2ModelAdpt.buildScrollableComponent(new IComponentFactory() {
			@Override
			public Component makeComponent() {
				JLabel label = new JLabel(img);
				return label;
			}
		}, name);
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
}
