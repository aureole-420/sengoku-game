package yt30_lp28.chatapp.mvc.model;

import java.awt.Component;

import common.IChatRoom;
import yt30_lp28.chatapp.impl.ProxyUser;
import yt30_lp28.chatapp.impl.Receiver;
import yt30_lp28.chatapp.impl.User;

/**
 * Adapter for main model talking to the view
 */
public interface MainModel2ViewAdapter {

	/**
	 * Create a mini mvc corresponding to a chat room
	 * @param chatRoom the chat room corresponding to the mini mvc
	 * @param user local user
	 * @param receiver the local receiver corresponding to a chat room 
	 * @return MiniMVCAdapter talking to this mini mvc 
	 */
	MiniMVCAdapter<ProxyUser> makeMiniMVC(IChatRoom chatRoom, User user, Receiver receiver);

	/**
	 * Append text to the view
	 * @param text text to add
	 */
	public void append(String text);

	/**
	 * Set server name in the view
	 * @param localAddr server name(local ip address) to be set 
	 */
	public void setServerName(String localAddr);

	/**
	 * Add a connected user in the view
	 * @param userStub the user stub to add
	 */
	public void addConnected(ProxyUser userStub);

	/**
	 * @param userStub
	 */
	public void removeConnected(ProxyUser userStub);

	/**
	 * Append message in the user level GUI
	 * @param text the message text
	 * @param label the label
	 */
	public void appendUserMsg(String text, String label);

	/**
	 * Add a scrollable component in the user level GUI
	 * @param comp the component to add
	 * @param label the label
	 */
	public void addScrollComp(Component comp, String label);

	/**
	 * Add a non-scrollable component in the user level GUI
	 * @param comp the component to add
	 * @param label the label
	 */
	public void addNonScrollComp(Component comp, String label);
}
