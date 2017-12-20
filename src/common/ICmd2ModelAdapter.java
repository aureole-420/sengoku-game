package common;

import provided.mixedData.MixedDataKey;

/**
 * This adapter is for the externally-sourced commands to  interact with the local system
 * @param <S> the target recipient type in method sendTo()
 * @param <M> The message type (must extends IMessageType)
 */
public interface ICmd2ModelAdapter<S, M extends IMessageType> {

	/**
	 * Get the name of the local user.
	 * @return the name of the local user
	 */
	public String getName();

	/**
	 * Append some message to the model, allowing the model to process the message by itself.
	 * Typically the model just appends to the view.
	 * @param text The message
	 * @param name The name of the person sending the text 
	 */
	public void appendMsg(String text, String name);

	/**
	 * Give the GUI a factory that creates a scrollable component component to add.
	 * Some components that are added are considered by the system to be just fancy displays of message contents and in such, 
	 * just like text messages, are desired to be placed on some sort of scrolling display.
	 * @param fac The factory to create the desired component
	 * @param label The identifying label for the scrollable component on the GUI
	 */
	public void buildScrollableComponent(IComponentFactory fac, String label);

	/**
	 * Give the GUI a factory that creates a non-scrollable component component to add.
	 * Some components that are added are actually static modifications of the local GUI to present additional user interaction capabilities.
	 * It would be undesireable for these components to scroll off the screen as other messages arrive;
	 * they should have a fixed location on the local GUI.
	 * @param fac the factory to create the desired component
	 * @param label The identifying label for the non-scrollable component on the GUI
	 */
	public void buildNonScrollableComponent(IComponentFactory fac, String label);

	/**
	 * Put a value into the local data storage(IMixedDataDictionary), associated with the given key,
	 * replacing any value already in the dictionary that is already associated
	 * with the key.
	 * Note that the local data storage(IMixedDataDictionary) is designed to be per-application.
	 * 
	 * @param key   the key to use to find the value
	 * @param value  The value to associate with the key
	 * @return The previous value associated with the key or null if there was none. 
	 * @param <T>  The type of data being put in
	 */
	public <T> T put(MixedDataKey<T> key, T value);

	/**
	 * Get the value associated with the given key from the local data storage(IMixedDataDictionary)
	 * Note that the local data storage(IMixedDataDictionary) is designed to be per-application.
	 * @param key  the key to use
	 * @return The value associated with the key or null if there is no entry for the key.
	 * @param <T> The type of data being retrieved
	 */
	public <T> T get(MixedDataKey<T> key);

	/**
	 * Send a data packet to a target recipient. 
	 * @param target the target recipient
	 * @param id the class id, i.e. message type
	 * @param data the data
	 */
	public <T extends M> void sendTo(S target, Class<T> id, T data);
}
