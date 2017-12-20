package yt30_lp28.sengokugame.clientSide;

import javax.swing.DefaultComboBoxModel;

import common.ICRMessageType;
import gov.nasa.worldwind.layers.Layer;
import yt30_lp28.sengokugame.serverSide.CityIcon;

/**
 * The adapter allows Game Model on client side to call view's method.
 * The "view" covers game view, main model etc.
 * @author yt30, lp28
 *
 */
public interface IClientGameModel2ViewAdapter {

	/**
	 * Add layer to the view's wwj panel
	 * @param layer the layer to add
	 */
	void addLayer(Layer layer);

	/**
	 * remove layer to the views wwj panel
	 * @param layer layer to remove
	 */
	void removeLayer(Layer layer);

	/**
	 * send message to server (implemented in cmd2ModelAdapter, not in game view)
	 * @param id the index of the message type
	 * @param data the message
	 */
	<T extends ICRMessageType> void sendTo(Class<T> id, T data);

	/**
	 * get the base city combo box model
	 * @return the base city combo box model
	 */
	public DefaultComboBoxModel<String> getCBModelCityTo();

	/**
	 * Get the target city combo box model
	 * @return the target city combo box model
	 */
	public DefaultComboBoxModel<String> getCBModelCityFrom();

	/**
	 * update the number of soldier in base city
	 * @param num update the number of soldier in base city
	 */
	void updateBaseSoldierNumber(int num);

	/**
	 * The selected Base city
	 * @return  The selected Base city
	 */
	String selectedFromCity();

}
