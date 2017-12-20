package yt30_lp28.sengokugame.serverSide;

import java.util.Collection;
import java.util.HashMap;

import yt30_lp28.sengokugame.serverSide.CityIcon;

/**
 * The adapter that allows cmd to call ClientGameAdapter's method
 * @author lp28,yt30
 *
 */
public interface ICmd2ClientGameAdapter {
	/**
	 * Update the cityIconList (in the game model on client side)
	 * @param remoteCityIcons the new cityIcon list (provided by the game server)
	 */
	public void updateCityIconList(HashMap<String, CityIcon> remoteCityIcons);

	/**
	 * update the game view on client side
	 */
	public void updateView();

	/**
	 * set city list for ComboBox (target city) in the game view 
	 */
	public void setToCityCBinView();

	/**
	 * set city list for ComboBox (base city) in the game view 
	 * @param occupiedCities a list of occupied cities;
	 */
	public void setFromCityCBinView(Collection<String> occupiedCities);

	/**
	 * Append intra team msg to view
	 * @param text the message to append
	 * @param label label for the message;
	 */
	public void appendIntraTeamMsg(String text, String label);

	/**
	 * Append inter team msg to game view;
	 * @param data the msg to append
	 * @param name the name of sender 
	 */
	public void appendInterTeamMsg(String data, String name);

	/**
	 * End the sengokuGame
	 * @param winner the winner of the game
	 */
	public void endSengokuGame(int winner);

	/**
	 * set/update the soldier assignment for this city; 
	 * @param allAssignment the new soldier assignment provided by the game server;
	 */
	public void setAllAssignment(HashMap<String, Integer> allAssignment);

	/**
	 * set initial view (of wwj map) (not actually in use)
	 */
	public void setInitialView();
}
