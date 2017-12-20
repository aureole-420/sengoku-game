package yt30_lp28.chatapp.mvc.model;

import java.awt.Component;

/**
 * Adapter for main model talking to minimvc
 * @param <T> the type of item in list of mini view
 */
public interface MiniMVCAdapter<T> {

	/**
	 * Add an item in the list of mini view
	 * @param t the item to add
	 */
	public void addItem(T t);

	/**
	 * Remove an item in the list of mini view
	 * @param t the item to remove
	 */
	public void removeItem(T t);

	/**
	 * Append a text in the mini view
	 * @param text the text to append
	 */
	public void append(String text);

	/**
	 * Add a scrollable component in the mini view
	 * @param comp  the scrollable component to add
	 * @param label the label with the component
	 */
	public void addScrollableComponent(Component comp, String label);

	/**
	 * Add a non-scrollable component in the mini view
	 * @param comp the non-scrollable component to add
	 * @param label the label with the component
	 */
	public void addNonScrollableComponent(Component comp, String label);

	/**
	 * Stop this mini mvc
	 */
	public void quit();
}
