package yt30_lp28.sengokugame.serverSide;

import java.util.HashMap;
import java.util.UUID;
import common.IReceiver;
import gov.nasa.worldwind.geom.Position;

/**
 * Player obj for the game
 */
public class Player {
	/**
	 * UUID of player, 
	 * this uuid should be the same as receiverstub's uuid;
	 */
	private UUID uuid; // 
	/**
	 * name of the player;
	 */
	private String name;
	/**
	 * the receiver stub of the player;
	 */
	private IReceiver _stub;
	/**
	 * Team number of the players;
	 */
	private int teamNo = 0;
	/**
	 * The number of soldiers assigned to each city
	 * NOTE: a city is not in cityAssignment.keySet() if the player has 0 soldier there. 
	 */
	private HashMap<City, Integer> cityAssignment = new HashMap<City, Integer>();

	/**
	 * attack range of the player, i.e., 1000 km in this case;
	 */
	private final double attackRange = 1000e3;

	/**
	 * Constructor of player
	 * @param uuid0 the uuid 
	 * @param stub0 the receiver stub of that player
	 * @param teamNo0 the team notation
	 * @param name0 name of the player
	 */
	public Player(UUID uuid0, IReceiver stub0, int teamNo0, String name0) {
		this.uuid = uuid0;
		this.teamNo = teamNo0;
		this._stub = stub0;
		this.name = name0;
	}

	/**
	 * getter for player's uuid
	 * @return the uuid
	 */
	public UUID getUUID() {
		return uuid;
	}

	/**
	 * getter for team number
	 * @return the team number
	 */
	public int getTeamNo() {
		return teamNo;
	}

	/**
	 * Getter for receiver stub
	 * @return the receiver stub 
	 */
	public IReceiver getReceiverStub() {
		return _stub;
	}

	public String getName() {
		return name;
	}

	/**
	 * check if two cities are close enough (that attack may occur)
	 * @param city1 the first city
	 * @param city2 the second city
	 * @return whether cities are close enough
	 */
	private boolean isTwoCityCloseEnough(City city1, City city2) {
		if (city1 == null || city2 == null) {
			return false;
		}

		double earthRadius = 6371e3;

		double distance = earthRadius * Position.greatCircleDistance(city1.getPosition(), city2.getPosition()).radians;

		// attackRange is final attribute
		if (distance <= attackRange * 1.01) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *  return false if not able to migrate
	 * @param cityFrom The base city
	 * @param cityTo the target city
	 * @param number number of soldiers to dispatch
	 * @return whether the action is possible
	 */
	public boolean migrate(City cityFrom, City cityTo, int number) {
		System.out.println("<<Player.migrate()>> tring to migrate");
		if (cityAssignment.get(cityFrom) < number) {
			// not enough citizens to imigrate from cityFrom;
			//System.out.println("<<Player.migrate()>> there are less people than possible to move: " + cityAssignment.get(cityFrom) + " < " + number);
			return false;
		}

		if (!isTwoCityCloseEnough(cityFrom, cityTo)) { // not close enough;
			return false;
		}
		cityAssignment.put(cityFrom, cityAssignment.get(cityFrom) - number);
		cityFrom.updateCityInfo(); // not really necessary?

		if (!cityAssignment.containsKey(cityTo)) {
			// if originally not in the cityTo;
			cityTo.addPlayer(this);
		}
		cityAssignment.put(cityTo, cityAssignment.getOrDefault(cityTo, 0) + number);
		cityTo.updateCityInfo(); // ? not really necessary?

		return true;
	}

	/**
	 * used when initially assign city to players;
	 * @param city the city to deploy soldiers.
	 * @param numCitizens the number of soldiers to dispatch
	 */
	public void deployToCity(City city, int numCitizens) {
		cityAssignment.put(city, numCitizens);
		city.addPlayer(this);
	}

	/**
	 * A method called indirectly by CITY: 
	 * when a player no longer has citizens in this city:
	 * two possible cases: 
	 * 1. the player retreats from the city, leaving no citizen in the city.
	 * 2. the citizens are all eliminated by enemies.
	 * @param city the city to remove
	 */
	public void removeCity(City city) {
		cityAssignment.remove(city);
	}

	/**
	 * called indirectly by the city 
	 * to update this player's deployment in a city
	 * @param city the city where this player is going to update his assignment there
	 * @param updatedNum the new number of soldier(that belongs to this player) in the city
	 */
	public void updateCityAssignment(City city, int updatedNum) {
		cityAssignment.put(city, updatedNum);
	}

	/**
	 * Get the number of soldiers in this city
	 * @param city a city
	 * @return the number of soldiers in this city
	 */
	public int getAssignment(City city) {
		return cityAssignment.getOrDefault(city, 0);
	}

	/**
	 * get all forces assignment of this exact player;
	 * @return all forces assignment of this exact player;
	 */
	public HashMap<String, Integer> getAllAssignment() {
		HashMap<String, Integer> allAssignment = new HashMap<>();
		for (City c : cityAssignment.keySet()) {
			allAssignment.put(c.getName(), getAssignment(c));
		}
		return allAssignment;
	}

}
