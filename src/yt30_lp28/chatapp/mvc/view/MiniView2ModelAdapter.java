package yt30_lp28.chatapp.mvc.view;

import java.awt.Component;

import javax.swing.ImageIcon;

/**
 * Adapter for mini view talking to model
 */
public interface MiniView2ModelAdapter {

	/**
	 * Delete a component
	 * @param comp the component to delete
	 */
	public void leave(Component comp);

	/**
	 * Send a text
	 * @param msg the text to send
	 */
	public void sendText(String msg);

	/**
	 * Send a image
	 * @param imageIcon the image to send
	 */
	public void sendImage(ImageIcon imageIcon);

	/**
	 * Send a non-scrollable component
	 */
	public void sendComp();
}
