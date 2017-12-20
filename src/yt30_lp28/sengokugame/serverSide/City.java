package yt30_lp28.sengokugame.serverSide;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.SurfaceCircle;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;

/**
 * The java class for city entity; Note that City Entity is only used on serverSide.
 * City can generate lighter, serializable CityIcon, which is transmitted to clientSide.
 * @author lp28, yt30
 */
public class City {
	/*
	 * Name of the City
	 */
	private final String _name;
	/**
	 * Position of the City (using geom....position)
	 * Note the CityIcon will have corresponding latitude/longitude in double for serialization.
	 */
	private final Position _position;

	/**
	 * Ownership denotes which Kingdom rules the city.
	 */
	private int _belongTo; // which team the city belongs to;

	/**
	 * Population of the city
	 */
	private int _population;

	/**
	 * Description of the city (automatically generated with above information.)
	 */
	private String _description; // a typical decription includes: ownership

	/**
	 * All warlords who have armed forces in the city.
	 */
	private HashSet<Player> occupants = new HashSet<Player>();

	/**
	 * Constructor of the city
	 * @param name name of the city
	 * @param pos position of the city
	 */
	public City(String name, Position pos) {
		_name = name;
		_position = pos;
		_belongTo = 0; // set to by default;
		_population = 0;
		_description = "City " + _name;
	}

	/**
	 * Population of the city grow naturally 
	 */
	public void populationGrow() {
		if (_population > 2000 || _population == 0) { // a city has maximum population of 2000
			return;
		}
		for (Player p : occupants) {
			if (p.getTeamNo() == 0) {
				continue; // The population of creep will never grow!
			}
			int numCtzs = p.getAssignment(this);
			//int n = occupants.size();
			numCtzs = (int) (numCtzs * 1.05 + (100.0 * numCtzs / _population));
			p.updateCityAssignment(this, numCtzs);
		}
		LinkedList<Player> templist = new LinkedList<>();
		for (Player p : occupants) { // initiate templist to avoid concurrant access to occupants
			templist.add(p);
		}
		for (Player p : templist) {
			if (p.getAssignment(this) == 0) {
				p.removeCity(this); // player no longer have citizens assigned to this city
				this.removePlayer(p); // this city no longer occupied by player p;
			}
		}

		updateCityInfo();

	}

	/**
	 * getter for occupants;
	 * @return the occupants
	 */
	public HashSet<Player> getOccupants() {
		return occupants;
	}

	/**
	 * Check if a player has armed force in this city.
	 * @param player a player/warlord
	 * @return true if there are armed forces there;
	 */
	public boolean hasOccupant(Player player) { //
		return occupants.contains(player);
	}

	/**
	 * Add warlords to the city
	 * @param player the player to add to the list;
	 */
	public void addPlayer(Player player) {
		occupants.add(player);
	}

	/**
	 * This method is called privately when updating city's status:
	 * after a turn: migration or battle may occur
	 * Any player with 0 citizens will be removed from the city.
	 * @param Player the player to remove from the list;
	 * */
	private void removePlayer(Player player) {
		occupants.remove(player);
	}

	/**
	 * Processing occupants:
	 * will affect (1) ownership (2) player's assignment
	 */
	public void updateOccupants() {
		int numCtzTm1 = 0;
		int numCtzTm2 = 0;
		int numCtzTm0 = 0; // creep

		// summarize population distribution:
		for (Player p : occupants) {
			if (p.getTeamNo() == 0) {
				numCtzTm0 += p.getAssignment(this);
			} else if (p.getTeamNo() == 1) {
				numCtzTm1 += p.getAssignment(this);
			} else {
				numCtzTm2 += p.getAssignment(this);
			}
		}

		// creeps hold the city
		if (numCtzTm0 >= numCtzTm1 + numCtzTm2) {
			// ownership keeps the same: creeps
			setOwnership(0);
			for (Player p : occupants) {
				if (p.getTeamNo() == 1 || p.getTeamNo() == 2) {
					p.updateCityAssignment(this, 0); // simply lost the population in battle field
				}
			}
			for (Player p : occupants) { // creeps
				if (p.getTeamNo() == 0) {
					int newNo = (numCtzTm0 - numCtzTm1 - numCtzTm2);
					newNo = newNo == 0 ? 1 : newNo;
					p.updateCityAssignment(this, newNo);
				}
			}
		}

		// otherwise creeps lost the city
		// team 1 win the war!
		else if (numCtzTm1 > numCtzTm2) {
			setOwnership(1);
			int totalLeft = (int) ((numCtzTm1 - numCtzTm2) * (1.0 - 1.0 * numCtzTm0 / (numCtzTm1 + numCtzTm2)));
			for (Player p : occupants) {
				if (p.getTeamNo() == 1) {
					// every participating warlords is left with at least 1 soldier;
					// if at begining 
					if (p.getAssignment(this) > 0) {
						int newNo = (int) (1.0 * totalLeft * p.getAssignment(this) / (0.0 + numCtzTm1));
						if (newNo == 0) {
							newNo++;
						}
						p.updateCityAssignment(this, newNo);
					}
				}
			}
			for (Player p : occupants) {
				if (p.getTeamNo() != 1) {
					p.updateCityAssignment(this, 0);
				}
			}
		}

		// team 2 win the war!
		else if (numCtzTm2 > numCtzTm1) {
			setOwnership(2);
			int totalLeft = (int) ((numCtzTm2 - numCtzTm1) * (1.0 - 1.0 * numCtzTm0 / (numCtzTm1 + numCtzTm2)));
			for (Player p : occupants) {
				if (p.getTeamNo() == 2) {
					// every participating warlords is left with at least 1 soldier;
					if (p.getAssignment(this) > 0) {
						int newNo = (int) (1.0 * totalLeft * p.getAssignment(this) / (0.0 + numCtzTm2));
						if (newNo == 0) {
							newNo++;
						}
						p.updateCityAssignment(this, newNo);
					}
				}
			}
			for (Player p : occupants) {
				if (p.getTeamNo() != 2) {
					p.updateCityAssignment(this, 0);
				}
			}
		}

		// Neither team 1 or 2 win the battle / when numCtzTm1 == numCtzTm2
		else {
			setOwnership(0);
			for (Player p : occupants) {
				if (p.getTeamNo() == 0) {
					p.updateCityAssignment(this, 1); // left one creep for this city
				} else {
					p.updateCityAssignment(this, 0);
				}
			}
		}

		// UPDATE the city
		LinkedList<Player> templist = new LinkedList<>();
		for (Player p : occupants) { // initiate templist to avoid concurrant access to occupants
			templist.add(p);
		}
		for (Player p : templist) {
			if (p.getAssignment(this) == 0) {
				p.removeCity(this); // player no longer have citizens assigned to this city
				this.removePlayer(p); // this city no longer occupied by player p;
			}
		}

		updateCityInfo();
	}

	/**
	 * update City population/ownership/description according to the occupants:
	 * ownership is only updated when in updateOccupants()
	 */
	public void updateCityInfo() {
		// ownership already updated

		// update population:
		int totPop = 0;
		for (Player p : occupants) {
			totPop += p.getAssignment(this);
		}
		setPopulation(totPop);

		// update the description: 
		StringBuilder descr = new StringBuilder();
		descr.append("City :" + _name + "\n");
		descr.append("Belonging to Kindom " + _belongTo + '\n');
		descr.append("==============\n");
		for (Player p : occupants) {
			descr.append("Lord " + p.getName() + ": " + p.getAssignment(this) + " soldiers\n");
		}
		setDescription(descr.toString());
	}

	/**
	 * getter for city name
	 * @return the city name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * getter for position 
	 * @return the position of the city
	 */
	public Position getPosition() {
		return _position;
	}

	/**
	 * getter for ownership
	 * @return the ownership of the city;
	 */
	public int getOwnership() {
		return _belongTo;
	}

	/**
	 * setter for ownership
	 * @param ownership the ownership of the city;
	 */
	public void setOwnership(int ownership) {
		_belongTo = ownership;
		System.out.println("<<City.setOwnership()>> city: " + _name + " set ownship to Kindom: " + _belongTo);
	}

	/**
	 * Getter for population
	 * @return the population of the city
	 */
	public int getPopulation() {
		return _population;
	}

	/**
	 * Setter for population
	 * @param pop The population of the city
	 */
	public void setPopulation(int pop) {
		_population = pop;
	}

	/**
	 * Getter for description
	 * @return The description of the city
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Setter for description
	 * @param desc The description of the city
	 */
	public void setDescription(String desc) {
		_description = desc;
	}

	/*
	public void updateCityIcon(CityInfo newInfo) {
		setOwnership(newInfo.getOwnership());
		setPopulation(newInfo.getPopulation());
		setDescription(newInfo.getDescription());
	}
	*/

	/*
	public void setToggledAnnotion(MapLayer _AnnoLayer) {
		_AnnoLayer.addToggleAnnotation(_name, _description, _position);
	}
	*/

	/**
	 * set Circleshape on wwj panel
	 * @param _ShapeLayer the shape layer
	 * @param teamColors the team colors
	 */
	public void setCircleShape(RenderableLayer _ShapeLayer, HashMap<Integer, Material> teamColors) {
		SurfaceCircle cityCircle = new SurfaceCircle(_position, getRadius(_population));
		_ShapeLayer.addRenderable(cityCircle);
		// set color
		ShapeAttributes attr = cityCircle.getAttributes();
		if (attr == null) {
			attr = new BasicShapeAttributes();
			attr.setInteriorMaterial(teamColors.get(_belongTo));
		}
		cityCircle.setAttributes(attr);
	}

	/**
	 * Getter for radius of the circle to draw on the map;
	 * @param pop The population of the city
	 * @return  The radius of the surfacecircle
	 */
	private double getRadius(int pop) { // compute radius of Surface Circle according to the population
		return 60e3 + (200e3) * Math.sqrt(pop / 100.0);
	}

	@Override
	/**
	 * overriden toString method
	 */
	public String toString() {
		return _name;
	}

	/**
	 * generate the CityIcon obj for the city obj
	 * @return the cityIcon obj
	 */
	public CityIcon generateCityIcon() {
		return new CityIcon(_name, _position.getLatitude().getDegrees(), _position.getLongitude().getDegrees(),
				_belongTo, _population, _description);
	}

	/**
	 * helper function for debugging;
	 * Print the city attributes;
	 */
	public void printCity() {
		System.out.println("\n----------------");
		System.out.println("City: " + _name);
		System.out.println("Ownership: " + _belongTo);
		System.out.println("Population: " + _population);
		System.out.println("Description: " + _description);
	}
}
