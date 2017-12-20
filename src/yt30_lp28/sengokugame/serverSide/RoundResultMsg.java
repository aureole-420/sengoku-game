package yt30_lp28.sengokugame.serverSide;

import java.util.HashMap;

import common.ICRMessageType;

/**
 * Round Result Msg Type for the server to notify the player's of 
 */
public class RoundResultMsg implements ICRMessageType {

	private static final long serialVersionUID = 2489305039717757579L;
	/**
	 * CityIcon list
	 */
	private HashMap<String, CityIcon> CityIcons;
	/**
	 * Assignment of soldiers for that specific player(the receiver)
	 */
	private HashMap<String, Integer> allAssignment;

	/**
	 * Constructor of round result msg
	 * @param CityIcons0 city icons  list
	 * @param allAssignment0 assignment list.
	 */
	public RoundResultMsg(HashMap<String, CityIcon> CityIcons0, HashMap<String, Integer> allAssignment0) {
		CityIcons = CityIcons0;
		allAssignment = allAssignment0;
	}

	/**
	 * getter for city icons.
	 * @return the city icon list
	 */
	public HashMap<String, CityIcon> getCityIcons() {
		return CityIcons;
	}

	/**
	 * The getter for soldier assignments.
	 * @return the soldier assignment.
	 */
	public HashMap<String, Integer> getAllAssignment() {
		return allAssignment;
	}

}
