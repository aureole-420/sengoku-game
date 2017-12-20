package yt30_lp28.chatapp.impl;

import common.ICRMessageType;
import common.IUserMessageType;

/**
 * Message type for sending String to remote user, can be used in both user and chat room level.
 */
public class MyString implements IUserMessageType, ICRMessageType {
	private static final long serialVersionUID = -7078630287513482645L;
	private String data;

	/**
	 * Constructor for MyString, takes a String object as parameter
	 * @param data the String object
	 */
	public MyString(String data) {
		this.data = data;
	}

	/**
	 * Get the data (String object)
	 * @return the data
	 */
	public String getData() {
		return this.data;
	}
}
