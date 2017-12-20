package yt30_lp28.sengokugame.clientSide;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;

import common.IReceiver;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Material;
import map.MapLayer;
import yt30_lp28.sengokugame.serverSide.CityIcon;
import yt30_lp28.sengokugame.serverSide.RoundDecisionMsg;

/**
 * The Game Model on client side.
 * The Game MVC on client side supports following functions:
 * 1. Visualize the city assignment based on a set of CityIcons.
 * @author yt30, lp28
 */
public class ClientGameModel {
	/**
	 * The server's receiver stub
	 */
	private IReceiver serverStub;
	/**
	 * adapter for the game view;
	 */
	private IClientGameModel2ViewAdapter _view;
	/**
	 * Surface Shape layer for the wwj map panel;
	 */
	private RenderableLayer _ShapeLayer;
	/**
	 * Annotation layer
	 */
	private MapLayer _AnnoLayer;
	/**
	 * The layer to draw range circle;
	 */
	private RenderableLayer _RangeLayer;
	/**
	 * City icon list;
	 */
	private HashMap<String, CityIcon> localCityIcons = new HashMap<String, CityIcon>(); // local city icon list.
	/**
	 * team color list;
	 */
	private HashMap<Integer, Material> _teamColors;
	/**
	 * all assignment of the player of this client
	 */
	private HashMap<String, Integer> allAssignment = new HashMap<String, Integer>(); // soldier assignment;

	/**
	 * Attack range.
	 */
	private final double attackRange = 1000e3;

	/**
	 * Constructor of client game model;
	 * @param serverStub0 the server stub
	 * @param View0 the game view adapter;
	 */
	public ClientGameModel(IReceiver serverStub0, IClientGameModel2ViewAdapter View0) {
		this.serverStub = serverStub0;
		this._view = View0;
		initModel();
	}

	/**
	 * initialize the game model;
	 */
	private void initModel() {
		// prepare ShapeLayer;
		_ShapeLayer = new RenderableLayer();
		// prepare _annoLayer
		_AnnoLayer = new MapLayer();
		// prepare _RangeLayer
		_RangeLayer = new RenderableLayer();

		_teamColors = new HashMap<Integer, Material>();
		_teamColors.put(1, Material.CYAN); // team1
		_teamColors.put(2, Material.YELLOW); // team 2
		_teamColors.put(0, Material.GRAY); // team creep

	}

	/**
	 * Update local CityIcons using the cityIconList passed from the server.
	 * That is, when each turn finished, the server is going to send new Icon to the 
	 * @param remoteCityIcons
	 */
	public void updateCityIconList(HashMap<String, CityIcon> remoteCityIcons) {
		for (String cname : remoteCityIcons.keySet()) {
			CityIcon remoteIcon = remoteCityIcons.get(cname);
			//_view.addToCityCB(remoteIcon.getName());
			if (localCityIcons.containsKey(cname)) {
				localCityIcons.get(cname).updateInformation(remoteIcon);
			} else {
				localCityIcons.put(cname, remoteIcon);
			}
		}
	}

	/**
	 * set the base city in view.
	 * @param occupiedCities The list of base cities;
	 */
	public void setFromCityCBinView(Collection<String> occupiedCities) {
		/*
		_view.removeFromCityAll();
		for (String c : occupiedCities) {
			_view.addFromCityCB(c);
		}
		*/
		DefaultComboBoxModel<String> FromCityList = _view.getCBModelCityFrom();
		LinkedList<String> FromCityListCopy = new LinkedList<String>();
		for (int i = 0; i < FromCityList.getSize(); i++) {
			FromCityListCopy.add(FromCityList.getElementAt(i));
		}
		for (String c : occupiedCities) {
			if (!FromCityListCopy.contains(c)) {
				//FromCityList.insertElementAt(c, FromCityList.getSize()-1);
				System.out.println("FromCityList.getSize()-1 : " + (FromCityList.getSize() - 1));
				int insertPosition = 0;
				if (FromCityList.getSize() - 1 > 0) {
					insertPosition = FromCityList.getSize() - 1;
				}
				FromCityList.insertElementAt(c, insertPosition);
			}
		}

		FromCityListCopy.clear();
		for (int i = 0; i < FromCityList.getSize(); i++) {
			FromCityListCopy.add(FromCityList.getElementAt(i));
		}
		for (String c : FromCityListCopy) {
			if (!occupiedCities.contains(c)) {
				FromCityList.removeElement(c);
			}
		}
	}

	/**
	 * set the target cities in view;
	 */
	public void setToCityCBinView() {
		/**
		System.out.println("<<ClientGameModel.setToCityCBinView()>> size of combo box " + localCityIcons.size());
		_view.removeToCityAll();
		for (String c : localCityIcons.keySet()) {
			_view.addToCityCB(c);
		}
		*/
		DefaultComboBoxModel<String> ToCityList = _view.getCBModelCityTo();
		LinkedList<String> ToCityListCopy = new LinkedList<String>();
		for (int i = 0; i < ToCityList.getSize(); i++) {
			ToCityListCopy.add(ToCityList.getElementAt(i));
		}
		for (String c : localCityIcons.keySet()) {
			if (!ToCityListCopy.contains(c)) {
				int insertPosition = 0;
				if (ToCityList.getSize() - 1 > 0) {
					insertPosition = ToCityList.getSize() - 1;
				}
				ToCityList.insertElementAt(c, insertPosition);
			}
		}
	}

	/**
	 * update the map in view
	 */
	public void updateView() { // updateModel

		// update shape layer 
		// 1. remove all the circles
		// 2. draw new circles according to cityIcons.
		_view.removeLayer(_ShapeLayer);
		_ShapeLayer.removeAllRenderables();
		for (String key : localCityIcons.keySet()) {
			//_cities.get(key).setCircleShape(_ShapeLayer, _teamColors);
			localCityIcons.get(key).setCircleShape(_ShapeLayer, _teamColors);
		}
		_view.addLayer(_ShapeLayer);

		//update annotation layer 
		_view.removeLayer(_AnnoLayer);
		_AnnoLayer.removeAllAnnotations();
		for (String key : localCityIcons.keySet()) {
			//_cities.get(key).setToggledAnnotion(_AnnoLayer);
			localCityIcons.get(key).setToggledAnnotion(_AnnoLayer);
		}
		_view.addLayer(_AnnoLayer);
	}

	/**
	 * Update the range layer
	 * @param BaseCity the base city being selected
	 */
	public void updateRangeLayer(String BaseCity) {
		_view.removeLayer(_RangeLayer);
		_RangeLayer.removeAllRenderables();
		if (BaseCity == null) {
			return;
		} else {
			if (localCityIcons.get(BaseCity) == null)
				return;
			localCityIcons.get(BaseCity).drawAttackRangeCircle(_RangeLayer);
		}
		_view.addLayer(_RangeLayer);

	}

	/**
	 * send decision to game server  
	 * @param cityFrom the base city
	 * @param cityTo the target city
	 * @param noCtz2Move number of soldiers being dispatched
	 */
	public void sendDecision(Object cityFrom, Object cityTo, int noCtz2Move) {
		if (cityFrom instanceof String && cityTo instanceof String) {
			// send a the decision to server
			//_view.sendTo(RoundDecisionMsg.class, new RoundDecisionMsg(((CityIcon) cityFrom).getName(), ((CityIcon) cityTo).getName(), noCtz2Move));
			_view.sendTo(RoundDecisionMsg.class,
					new RoundDecisionMsg((String) cityFrom, (String) cityTo, noCtz2Move, false));

		} else {
			System.out.println("<<ClientGameModel.sendDecision()>> cityFrom or cityTo not instanceof String! cityFrom"
					+ cityFrom.getClass());
		}
	}

	/**
	 * Start the model
	 */
	public void start() {

	}

	/**
	 * Setter for armed force assignment
	 * @param allAssignment the new armed force assignment provided by game server;
	 */
	public void setAllAssignment(HashMap<String, Integer> allAssignment) {
		this.allAssignment = allAssignment;
		// update view's base soldiers
		int num = allAssignment.getOrDefault(_view.selectedFromCity(), 0);
		_view.updateBaseSoldierNumber(num);
	}

	/**
	 * Getter for number of soldiers in base city
	 * @param city the base city
	 * @return  number of soldiers in base city
	 */
	public int getNumSoldiersInBase(String city) {
		return allAssignment.getOrDefault(city, 0);
	}

	/**
	 * Getter for selected city
	 * @param pos position of mouse click
	 * @return a selected city (if mouse click succeed)
	 */
	private String getSelectedCity(Position pos) {
		for (String cname : localCityIcons.keySet()) {
			CityIcon cIcon = localCityIcons.get(cname);
			if (clickCityWithinRange(pos, cIcon)) {
				return cIcon.getName();
			}
		}
		return null;
	}

	/**
	 * check whether the mouse click select a city successfully
	 * @param pos the position of a mouse click
	 * @param cIcon theIcon of a city
	 * @return whether the mouse click select a city successfully
	 */
	private boolean clickCityWithinRange(Position pos, CityIcon cIcon) {
		if (pos == null) {
			return false;
		}

		int population = cIcon.getPopulation();

		double earthRadius = 6371e3;
		double rangeRadius = 30e3 + (20e3) * Math.sqrt(population / 100.0);

		double clat = cIcon.getLatitude();
		double clon = cIcon.getLongitude();

		double distance = earthRadius * Position.greatCircleDistance(pos, Position.fromDegrees(clat, clon)).radians;
		if (cIcon.getName().equals("New York City")) {
			System.out.println("distance between click and CITY:: " + distance);
			System.out.println("rangeRadius :: " + rangeRadius);
			System.out.println(
					"reference range radius :: " + (rangeRadius + 100e3 * Math.exp(-2 * rangeRadius / (30e3))));
			System.out.println("distance in radius :: "
					+ Position.greatCircleDistance(pos, Position.fromDegrees(clat, clon)).radians);
		}

		if (distance <= rangeRadius + 100e3 * Math.exp(-2 * rangeRadius / (30e3))) {
			System.out.println("---######----- WITHIN RANGE ---#########------");
			return true;
		} else {
			//System.out.println("-------- OUT OF IN RANGE --------");
			return false;
		}
	}

	/**
	 * check is two city close enough 
	 * @param city1 the first city
	 * @param city2 the second city
	 * @return whether two cities are close enough
	 */
	private boolean isTwoCityCloseEnough(CityIcon city1, CityIcon city2) {
		if (city1 == null || city2 == null) {
			return false;
		}

		double clat1 = city1.getLatitude();
		double clon1 = city1.getLongitude();

		double clat2 = city2.getLatitude();
		double clon2 = city2.getLongitude();
		double earthRadius = 6371e3;

		double distance = earthRadius * Position.greatCircleDistance(Position.fromDegrees(clat1, clon1),
				Position.fromDegrees(clat2, clon2)).radians;

		// attackRange is final attribute
		if (distance <= attackRange * 1.01) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Set left lick action.
	 * @param pos the position of mouse click on the map
	 */
	public void setLeftClickBaseCity(Position pos) {
		String city = getSelectedCity(pos);
		if (city == null) {
			return;
		}
		if (allAssignment.keySet().contains(city)) {
			// set to city....
			_view.getCBModelCityFrom().setSelectedItem(city);
			_view.updateBaseSoldierNumber(allAssignment.get(city));
		}
	}

	/**set right click action
	 * @param pos the position of mouse click on the map
	 */
	public void setRightClickTargetCity(Position pos) {
		String city = getSelectedCity(pos);
		if (city == null) { // no city can be selected with pos;
			return;
		}
		if (localCityIcons.keySet().contains(city)) {
			String SelectedBaseCity = (String) _view.getCBModelCityFrom().getSelectedItem();
			if (SelectedBaseCity == null) {
				return; // BaseCity not selected;
			} else {
				CityIcon ci1 = localCityIcons.get(city);
				CityIcon ci2 = localCityIcons.get(SelectedBaseCity);
				if (!isTwoCityCloseEnough(ci1, ci2)) { // two city not close enough;
					return;
				}
			}
			_view.getCBModelCityTo().setSelectedItem(city);
		}
	}

}
