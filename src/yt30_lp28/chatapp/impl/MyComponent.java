package yt30_lp28.chatapp.impl;

import common.ICRMessageType;
import common.IUserMessageType;

/**
 * Message type for sending a component to remote peers, can be used in both user level and chat room level.
 */
public class MyComponent implements ICRMessageType, IUserMessageType {
	private static final long serialVersionUID = -147210137008349599L;
}
