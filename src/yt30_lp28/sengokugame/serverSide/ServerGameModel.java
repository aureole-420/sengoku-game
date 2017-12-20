package yt30_lp28.sengokugame.serverSide;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import common.DataPacketCR;
import common.IChatRoom;
import common.IReceiver;
import gov.nasa.worldwind.geom.Position;

/**
 * The game model on the server side. 
 * This game model (server side) is initiated first.
 * 1. send a game mvc to the client
 * 2. once the client installs the mvc. 
 * 3. the game model on server side can send a model to the client to start the game.
 *  
 * @author yt30, lp28 
 *
 */
public class ServerGameModel {
	/**
	 * game team 1
	 */
	private IChatRoom team1;
	/**
	 * game team 2
	 */
	private IChatRoom team2;
	/**
	 * kingdom1 corresponding to team 1
	 */
	private HashMap<UUID, Player> kingdom1 = new HashMap<UUID, Player>();
	/**
	 * kingdom2 corresponding to team 2
	 */
	private HashMap<UUID, Player> kingdom2 = new HashMap<UUID, Player>();
	/**
	 * Creep player (computer with no AI)
	 */
	private Player creepPlayer;

	/**
	 * City list
	 */
	private HashMap<String, City> cities = new HashMap<>();
	/**
	 * City icon list;
	 */
	private HashMap<String, CityIcon> cityIcons = new HashMap<>();

	/**
	 * The forces assignment of each warlord
	 */
	private HashMap<UUID, HashMap<String, Integer>> warlordAssignment = new HashMap<>();
	/**
	 * The adapter that allows calling server Model's methods.
	 */
	private IServerGameModel2ServerModelAdapter serverModel;

	/**
	 * IMPORTANT!
	 * a concurrent message queue that deals with msgs sending from game clinet.
	 */
	private LinkedBlockingDeque<DataPacketCR<RoundDecisionMsg>> DecisionMsgQueue;

	/**
	 * Constructor of GameModel on serverside
	 * @param t1 the chat room for team 1
	 * @param t2 the chat room for team 2
	 * @param serverModel0 the server Model adapter 
	 */
	public ServerGameModel(IChatRoom t1, IChatRoom t2, IServerGameModel2ServerModelAdapter serverModel0) {
		this.team1 = t1;
		this.team2 = t2;
		this.serverModel = serverModel0;
		initGameSettings();
		initDecisionMsgQueue();
		initPopulationGrowing();
	}

	/**
	 * Initialize the decision msg queue;
	 */
	private void initDecisionMsgQueue() {
		DecisionMsgQueue = new LinkedBlockingDeque<DataPacketCR<RoundDecisionMsg>>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (!DecisionMsgQueue.isEmpty()) {
						DataPacketCR<RoundDecisionMsg> host = DecisionMsgQueue.poll();

						RoundDecisionMsg decision = host.getData();
						String cityFrom = decision.getCityFrom();
						String cityTo = decision.getCityTo();
						int noCtz2Move = decision.getNoCtz2Move();
						boolean isPopulationGrowing = decision.getIsPopulationGrowing();

						if (isPopulationGrowing) { // 
							populationGrow();
						} else {
							UUID uuid = null;
							try {
								uuid = host.getSender().getUUID();
							} catch (RemoteException e) {
								System.out.println(
										"<<ServerGameModel.initDecisionMsgQueue()>> RemoteException getting uuid from receiver");
								e.printStackTrace();
							}
							playerTakeAction(uuid, cityFrom, cityTo, noCtz2Move);
						}

						notifyResult4Turn();
					}
				}
			}
		}).start();
	}

	/**
	 * initialize a thread to deal with the population growing
	 */
	private void initPopulationGrowing() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//DecisionMsgQueue.
					//populationGrow();
					//notifyResult4Turn();

					DecisionMsgQueue.addFirst(new DataPacketCR<RoundDecisionMsg>(RoundDecisionMsg.class,
							new RoundDecisionMsg(null, null, 0, true), null));
					;
				}
			}

		}).start();
	}

	/**
	 * let population grow
	 */
	private void populationGrow() {
		for (String cname : cities.keySet()) {
			City c = cities.get(cname);
			c.populationGrow();
		}
	}

	/**
	 * getter for decision msg queue;
	 * @return the decision msg queue;
	 */
	public LinkedBlockingDeque<DataPacketCR<RoundDecisionMsg>> getDecisionMsgQueue() {
		return DecisionMsgQueue;
	}

	/**
	 * several steps to init the game:
	 * 1. initialize the player;
	 * 2. initialize the city;
	 * 3. assign the city to player;
	 */
	public void initGameSettings() {
		initPlayers();
		initCities();
		assignCities();
		updateCities();
		updateCityIcons();
		updateWarlordAssignment();
	}

	/**
	 * Initialize all players for two teams;
	 */
	private void initPlayers() {
		try { // crate players for team1 or 2.
			for (IReceiver stub : team1.getIReceiverStubs()) {
				UUID userID = serverModel.getUserID();
				if (stub.getUUID().equals(userID)) {
					continue;
				}
				kingdom1.put(stub.getUUID(), new Player(stub.getUUID(), stub, 1, stub.getUserStub().getName()));
			}
			for (IReceiver stub : team2.getIReceiverStubs()) {
				UUID userID = serverModel.getUserID();
				if (stub.getUUID().equals(userID)) {
					continue;
				}
				kingdom2.put(stub.getUUID(), new Player(stub.getUUID(), stub, 2, stub.getUserStub().getName()));
			}
		} catch (RemoteException e) {
			System.err
					.println("<<ServerGameModel.constructor()>> remote exception creating players for team1 or team2");
			e.printStackTrace();
		}
		// init creepPlayer
		creepPlayer = new Player(null, null, 0, "Lich King");
	}

	/**
	 * initialize two cities;
	 */
	private void initCities() {
		cities.put("Washington", new City("Washington", Position.fromDegrees(38.54, -77.02, 1000)));
		cities.put("Pittsburgh", new City("Pittsburgh", Position.fromDegrees(40.27, -80, 1000)));
		cities.put("New York City", new City("New York City", Position.fromDegrees(40.71, -74.006, 1000)));
		cities.put("Houston", new City("Houston", Position.fromDegrees(29.76, -95.36, 1000)));

		cities.put("Chicago", new City("Chicago", Position.fromDegrees(41.53, -87.38, 1000)));
		cities.put("Seattle", new City("Seattle", Position.fromDegrees(47.37, -122.20, 1000)));
		cities.put("Miami", new City("Miami", Position.fromDegrees(25.7617, -80.1918, 1000)));
		cities.put("Los Angeles", new City("Los Angeles", Position.fromDegrees(34.03, -118.15, 1000)));

		cities.put("San Francisco", new City("San Francisco", Position.fromDegrees(37.47, -122.25, 1000)));
		cities.put("Las Vegas", new City("Las Vegas", Position.fromDegrees(36.11, -115.08, 1000)));
		cities.put("Atlanta", new City("Atlanta", Position.fromDegrees(33.45, -84.23, 1000)));
		cities.put("St. Louis", new City("St. Louis", Position.fromDegrees(38.6270, -90.1994, 1000)));
		cities.put("Kansas City", new City("Kansas City", Position.fromDegrees(39.0997, -94.5786, 1000)));
		cities.put("Oklahoma City", new City("Oklahoma City", Position.fromDegrees(35.4676, -97.5164, 1000)));
		cities.put("Phoenix", new City("Phoenix", Position.fromDegrees(33.4484, -112.0740, 1000)));
		cities.put("Denver", new City("Denver", Position.fromDegrees(39.7392, -104.9903, 1000)));
		cities.put("New Orleans", new City("New Orleans", Position.fromDegrees(29.9511, -90.0715, 1000)));
		cities.put("Portland", new City("Portland", Position.fromDegrees(45.5231, -122.6765, 1000)));

	}

	/**
	 * update city objs
	 */
	private void updateCities() {
		for (String cname : cities.keySet()) {
			cities.get(cname).updateCityInfo();
			cities.get(cname).printCity();
		}
	}

	/**
	 * update city icon list;
	 */
	private void updateCityIcons() {
		for (String cname : cities.keySet()) {
			CityIcon newIcon = cities.get(cname).generateCityIcon();
			cityIcons.put(cname, newIcon);
		}
	}

	/**
	 * update the force assignment for all warlords;
	 */
	private void updateWarlordAssignment() {
		for (UUID uuid : kingdom1.keySet()) {
			Player p = kingdom1.get(uuid);
			warlordAssignment.put(uuid, p.getAllAssignment());
		}
		for (UUID uuid : kingdom2.keySet()) {
			Player p = kingdom2.get(uuid);
			warlordAssignment.put(uuid, p.getAllAssignment());
		}
	}

	/**
	 * Initial city assignment.
	 * Firstly assign cities to players.
	 * All the rest cities are assigned to creepPlayer;
	 */
	private void assignCities() {
		LinkedList<City> cityQueue = new LinkedList<City>();
		for (String cname : cities.keySet()) {
			cityQueue.offer(cities.get(cname));
		}

		LinkedList<Player> playerQueue = new LinkedList<Player>();
		for (UUID uid : kingdom1.keySet()) {
			playerQueue.offer(kingdom1.get(uid));
		}
		for (UUID uid : kingdom2.keySet()) {
			playerQueue.offer(kingdom2.get(uid));
		}

		while (!cityQueue.isEmpty() && !playerQueue.isEmpty()) {
			Player p = playerQueue.poll();
			City c = cityQueue.poll();
			p.deployToCity(c, 1000);
			System.out.println("<<ServerGameModel.assignCities()>> Player: " + p.getName() + " is assigned to city: "
					+ c.getName());
		}
		while (!cityQueue.isEmpty()) {
			City c = cityQueue.poll();
			creepPlayer.deployToCity(c, 100);
		}
	}

	/**
	 * Carry out player's action
	 * @param uuid the uuid of player;
	 * @param cityFrom The base city
	 * @param cityTo the target city
	 * @param number the number of soldier to send
	 */
	public void playerTakeAction(UUID uuid, String cityFrom, String cityTo, int number) {
		Player p;
		if (kingdom1.containsKey(uuid)) {
			p = kingdom1.get(uuid);
		} else {
			p = kingdom2.get(uuid); // should be able to get player from here;
		}
		City cFrom = cities.get(cityFrom);
		City cTo = cities.get(cityTo);
		p.migrate(cFrom, cTo, number);
	}

	/**
	 * do the update when a turn finishes: 
	 * 1. Each player take their individual action using playerTakeAction(); which will affect a cities soldier assignments.
	 * 2. The cities are updated based on all soldier assignment (there will be battles if multiple teams reside in the same city).
	 * Step 2 is implemented using updateAfterATurn()
	 */
	public void updateAfterATurn() {
		// a) update each city's own information;
		for (String cname : cities.keySet()) {
			cities.get(cname).updateOccupants();
		}
		// b) update the cityIcon.
		updateCityIcons();
		updateWarlordAssignment();
		// c) update each players information
	}

	/**
	 * Notify the result (new CityIcons) to all players 
	 */
	public void notifyResult4Turn() {
		updateAfterATurn();
		System.out.println("--------------------ServerGameModel.notifyResult4Turn()------------------------");
		for (String cname : cityIcons.keySet()) {
			CityIcon c = cityIcons.get(cname);
			c.printCityIcon();
		}
		System.out.println("--------------------ServerGameModel.notifyResult4Turn()------------------------");

		//		int winner = checkWinner();
		//		serverModel.broadcast(SengokuGameEndMsg.class, new SengokuGameEndMsg(winner));

		// check if any team wins the game:
		if (TeamWin(1)) {
			// broadcast that team 1 wins
			serverModel.broadcast(SengokuGameEndMsg.class, new SengokuGameEndMsg(1));
		} else if (TeamWin(2)) {
			// broadcast that team 2 wins
			serverModel.broadcast(SengokuGameEndMsg.class, new SengokuGameEndMsg(2));
		} else if (TeamWin(0)) {
			// broadcast that team 0 wins;
			serverModel.broadcast(SengokuGameEndMsg.class, new SengokuGameEndMsg(0));
		} else {
			// continue the game;
		}

		// send result for a turn;
		HashMap<UUID, RoundResultMsg> dataPackets = new HashMap<>();
		for (UUID uuid : warlordAssignment.keySet()) {
			dataPackets.put(uuid, new RoundResultMsg(cityIcons, warlordAssignment.get(uuid)));
		}
		serverModel.broadCastNonUniform(RoundResultMsg.class, dataPackets);
	}

	/**
	 * Check if any team occupied all the cities.
	 */
	private boolean TeamWin(int teamNo) {
		for (String cname : cities.keySet()) {
			if (cities.get(cname).getOwnership() != teamNo) {
				return false;
			}
		}
		return true;
	}

	/**
	 * An alternative winning criteria;
	 * @return whether there is a winner;
	 */
	private int checkWinner() {
		boolean kingdom1Eliminated = true;
		boolean kingdom2Eliminated = true;
		for (Player p : kingdom1.values()) {
			if (p.getAllAssignment().size() != 0)
				kingdom1Eliminated = false;
		}
		for (Player p : kingdom2.values()) {
			if (p.getAllAssignment().size() != 0)
				kingdom2Eliminated = false;
		}
		if (kingdom1Eliminated && kingdom2Eliminated)
			return 0;
		else if (kingdom1Eliminated)
			return 2;
		else if (kingdom2Eliminated)
			return 1;
		return 0;
	}

}
