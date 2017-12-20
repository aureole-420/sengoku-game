package yt30_lp28.chatapp.mvc.view;

/**
 * Adapter for main view talking to main model
 * @param <TDropListItem> type of item in the drop list
 */
public interface MainView2ModelAdapter<TDropListItem> {

	/**
	 * Create a chat room with a given name
	 * @param name the name of the chat room
	 */
	public void createChatRoom(String name);

	/**
	 * Connect to remote user by ip address
	 * @param remoteIP remote ip address
	 */
	public void connectTo(String remoteIP);

	/**
	 * Stop the model
	 */
	public void stop();

	/**
	 * Request from the selected item
	 * @param item the given item
	 */
	public void request(TDropListItem item);

	/**
	 * Start the server using given user name and server name
	 * @param name the user name
	 * @param addr the server name(address)
	 */
	public void startServer(String name, String addr);

	/**
	 * Send a message to target recipient
	 * @param text the message to send
	 * @param target the target recipient
	 */
	public void sendMsg(String text, TDropListItem target);

	/**
	 * Send a component to target recipient
	 * @param target the target recipient
	 */
	public void sendComp(TDropListItem target);
}
