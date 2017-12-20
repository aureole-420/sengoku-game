package yt30_lp28.sengokugame.serverSide;

import common.ICRMessageType;

/*
 * The msg type containing player's decision (actions) in this round 
 */
public class RoundDecisionMsg implements ICRMessageType {
	private static final long serialVersionUID = 6373595778968005312L;
	/**
	 * the base city
	 */
	private String cityFrom;
	/**
	 * the target city
	 */
	private String cityTo;
	/**
	 * Number of soldiers to dispatch
	 */
	private int noCtz2Move;
	/**
	 * NOTE: populationGrowing is a special RoundDecisionMsg (actually send from server to server !!! -- we did this simply because 
	 * we wish to make use of the BlockingQueue) 
	 */
	private boolean isPopulationGrowing = false;

	/**
	 *  Constructor of round decision msg
	 * @param cityFrom0 the base city
	 * @param cityTo0 the target city
	 * @param noCtz2Move0  Number of soldiers to dispatch
	 * @param isPopulationGrowing0 flag for msg type
	 */
	public RoundDecisionMsg(String cityFrom0, String cityTo0, int noCtz2Move0, boolean isPopulationGrowing0) {
		this.cityFrom = cityFrom0;
		this.cityTo = cityTo0;
		this.noCtz2Move = noCtz2Move0;
		this.isPopulationGrowing = isPopulationGrowing0;
	}

	/**
	 * Getter for base city
	 * @return the base city
	 */
	public String getCityFrom() {
		return cityFrom;
	}

	/**
	 * getter for target city
	 * @return the target city
	 */
	public String getCityTo() {
		return cityTo;
	}

	/**
	 * Number of soldiers to dispatch
	 * @return the number of soldier to dispatch
	 */
	public int getNoCtz2Move() {
		return noCtz2Move;
	}

	/**
	 * getter for the flag.
	 * @return
	 */
	public boolean getIsPopulationGrowing() {
		return isPopulationGrowing;
	}
}
