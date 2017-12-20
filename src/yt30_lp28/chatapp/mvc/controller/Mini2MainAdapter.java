package yt30_lp28.chatapp.mvc.controller;

import java.awt.Component;

/**
 * Adapter for mini-mvc talking with main-mvc
 */
public interface Mini2MainAdapter {

	/**
	 * Remove a mini view from the main view
	 * @param miniView the mini view to remove
	 */
	public void remove(Component miniView);

}
