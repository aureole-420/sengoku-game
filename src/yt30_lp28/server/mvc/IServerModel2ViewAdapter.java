package yt30_lp28.server.mvc;

import common.IChatRoom;
import yt30_lp28.chatapp.impl.ProxyUser;
import yt30_lp28.chatapp.impl.User;
import yt30_lp28.chatapp.mvc.model.MiniMVCAdapter;
import yt30_lp28.server.impl.ServerReceiver;

/**
 * MVC model2view adapter 
 */
public interface IServerModel2ViewAdapter {

	/**
	 * Create a mini mvc corresponding to a chat room
	 * @param chatRoom the chat room corresponding to the mini mvc
	 * @param user local user
	 * @param receiver the local receiver corresponding to a chat room 
	 * @return MiniMVCAdapter talking to this mini mvc 
	 */
	MiniMVCAdapter<ProxyUser> makeMiniMVC(IChatRoom chatRoom, User user, ServerReceiver receiver);

	/**
	 * Append text to the view
	 * @param text text to add
	 */
	public void append(String text);

	/**
	 * Set user name in the view
	 * @param name
	 */
	public void setUserName(String name);

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
	 * remove a connected user in the view
	 * @param userStub the user stub to remove
	 */
	public void removeConnected(ProxyUser userStub);
}
