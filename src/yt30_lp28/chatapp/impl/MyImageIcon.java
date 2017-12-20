package yt30_lp28.chatapp.impl;

import javax.swing.ImageIcon;

import common.ICRMessageType;
import common.IUserMessageType;

/**
 * Message type for sending images to remote peers, can be used in both user level and chat room level.
 */
public class MyImageIcon implements ICRMessageType, IUserMessageType {
	private static final long serialVersionUID = -3902442671430309780L;
	private ImageIcon image;

	/**
	 * Constructor for MyImageIcon
	 * @param image the ImageIcon object
	 */
	public MyImageIcon(ImageIcon image) {
		this.image = image;
	}

	/**
	 * Get the image
	 * @return the ImageIcon object
	 */
	public ImageIcon getImage() {
		return image;
	}
}
