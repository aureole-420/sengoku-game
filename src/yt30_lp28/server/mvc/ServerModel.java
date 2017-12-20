package yt30_lp28.server.mvc;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import common.DataPacketCR;
import common.DataPacketUser;
import common.ICRMessageType;
import common.IChatRoom;
import common.IComponentFactory;
import common.IReceiver;
import common.IUser;
import common.IUserMessageType;
import common.datatype.user.IInvitationType;
import provided.mixedData.MixedDataKey;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;
import yt30_lp28.sengokugame.serverSide.IServerGameModel2ServerModelAdapter;
import yt30_lp28.sengokugame.serverSide.SengokuGameMsg;
import yt30_lp28.sengokugame.serverSide.ServerGameModel;
import yt30_lp28.chatapp.impl.ProxyUser;
import yt30_lp28.server.cmd.ChooseTeamMsg;
import yt30_lp28.server.cmd.Invitation;
import yt30_lp28.server.demogame.DemoGameMsg;
import yt30_lp28.server.impl.LobbyReceiver;
import yt30_lp28.server.impl.ServerCRCmd2ModelAdapter;
import yt30_lp28.server.impl.ServerChatRoom;
import yt30_lp28.server.impl.ServerReceiver;
import yt30_lp28.server.impl.ServerUser;

/**
 * MVC model in the server side
 */
public class ServerModel {

	private IServerModel2ViewAdapter model2ViewAdpt;
	private Consumer<String> consumer;
	private IRMIUtils rmiUtils;
	private Registry registry = null;
	private ServerUser user;
	private IUser userStub;
	private int receivePort = IRMI_Defs.STUB_PORT_EXTRA + 1;
	private List<IChatRoom> rooms = new LinkedList<>();
	private List<ServerCRManager> managers = new LinkedList<>();
	private IChatRoom lobby;
	private ServerCRManager lobbyCr;

	private ServerGameModel _sengokuModel;

	private IChatRoom team1room, team2room;

	/**
	 * @param model2ViewAdpt
	 */
	public ServerModel(IServerModel2ViewAdapter model2ViewAdpt) {
		this.model2ViewAdpt = model2ViewAdpt;
	}

	private class ServerCRManager {
		ServerReceiver receiver;
		IReceiver receiverStub;
		IChatRoom room;

		ServerCRManager(IChatRoom room, boolean isLobbyCRManager) {
			this.room = room;
			ServerCRCmd2ModelAdapter cmd2Model = new ServerCRCmd2ModelAdapter(room) {
				@Override
				public <T extends ICRMessageType> void sendTo(IReceiver target, Class<T> id, T data) {
					try {
						target.receive(new DataPacketCR<T>(id, data, receiverStub));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}

				@Override
				public List<IChatRoom> getChatRooms() {
					return rooms;
				}

				@Override
				public String getName() {
					return null;
				}

				@Override
				public void inviteToChatRoom(IUser invitedUser, IChatRoom lobbyRoom) {
					try {
						invitedUser.receive(new DataPacketUser<IInvitationType>(IInvitationType.class,
								new Invitation(lobbyRoom), receiverStub.getUserStub()));
					} catch (RemoteException e) {
						System.out.println(
								"<<<ServerModel.serverCRManager>>> Exception sending invitation message: from :: "
										+ receiverStub + " ::to:: " + invitedUser);
						e.printStackTrace();
					}
				}

				@Override
				public ServerGameModel getSengokuGameModel() {
					return _sengokuModel;
				}
			};
			if (isLobbyCRManager) {
				receiver = new LobbyReceiver(room, user, userStub, cmd2Model);
			} else {
				receiver = new ServerReceiver(room, user, userStub, cmd2Model);
			}

			try {
				receiverStub = (IReceiver) UnicastRemoteObject.exportObject(receiver, receivePort);
				receiver.setReceiverStub(receiverStub);
				receiver.init();
				receivePort += 2;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			room.addIReceiverStub(receiverStub);
		}

		public <T extends ICRMessageType> void broadCast(Class<T> id, T data) {
			room.sendPacket(new DataPacketCR<T>(id, data, receiverStub));
		}

		public <T extends ICRMessageType> void broadCastNonUniform(Class<T> id, HashMap<UUID, T> uuidDataPairs) {
			HashMap<UUID, DataPacketCR<T>> dataPackets = new HashMap<UUID, DataPacketCR<T>>();
			for (UUID uuid : uuidDataPairs.keySet()) {
				dataPackets.put(uuid, new DataPacketCR<T>(id, uuidDataPairs.get(uuid), receiverStub));
			}
			((ServerChatRoom) room).sendNonUnifrom(dataPackets);
		}
	}

	/**
	 * Start the server model, do the following things:
	 * 1. Start the RMI server on IRMI_Defs.CLASS_SERVER_PORT_SERVER;
	 * 2. New a default user, and bind the stub to registry;
	 * 3. Create a lobby room.
	 */
	public void start() {
		consumer = new Consumer<String>() {
			@Override
			public void accept(String t) {
				model2ViewAdpt.append(t);
			}
		};
		rmiUtils = new RMIUtils(consumer);
		rmiUtils.startRMI(IRMI_Defs.CLASS_SERVER_PORT_SERVER);
		String addr = "localhost";
		try {
			addr = rmiUtils.getLocalAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String userName = "Game Server @ " + addr;
		user = new ServerUser(userName, new IServerUser2ModelAdapter() {
			@Override
			public void addConnected(IUser userStub) {
				model2ViewAdpt.addConnected(new ProxyUser(userStub));
			}

			@Override
			public void joinChatRoom(IChatRoom room) {
				System.out.println("[ServerUser joinChatRoom] Not implemented ----------- ");
			}

			@Override
			public void appendMsg(String text, String label) {
				System.out.println("[ServerUser appendMsg] Not implemented ---------------");
			}

			@Override
			public void buildScrollableComponent(IComponentFactory fac, String label) {
				System.out.println("[ServerUser addScrollComp] Not implemented ---------------");
			}

			@Override
			public void buildNonScrollableComponent(IComponentFactory fac, String label) {
				System.out.println("[ServerUser addNonScrollComp] Not implemented ---------------");
			}

			@Override
			public <T> T put(MixedDataKey<T> key, T value) {
				return null;
			}

			@Override
			public <T> T get(MixedDataKey<T> key) {
				return null;
			}

			@Override
			public <T extends IUserMessageType> void sendTo(IUser target, Class<T> id, T data) {
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public IChatRoom getLobbyRoom() {
				return lobby;
			}

			@Override
			public void removeConnected(IUser userStub) {
				model2ViewAdpt.removeConnected(new ProxyUser(userStub));
			}

			@Override
			public List<IChatRoom> getTeams() {
				return rooms;
			}
		});

		model2ViewAdpt.setServerName(addr);
		model2ViewAdpt.setUserName(userName);

		registry = rmiUtils.getLocalRegistry();
		try {
			userStub = (IUser) UnicastRemoteObject.exportObject(user, IRMI_Defs.STUB_PORT_SERVER);
			user.setSelfStub(userStub);
		} catch (RemoteException e) {
			consumer.accept("[ServerModel.init()]: Exception when export object " + e.getMessage());
			e.printStackTrace();
		}
		try {
			registry.rebind(IUser.BOUND_NAME, userStub);
			model2ViewAdpt.addConnected(new ProxyUser(userStub));
			consumer.accept("[ServerModel.init()]: Registry successfully bind " + IUser.BOUND_NAME + " To "
					+ IRMI_Defs.STUB_PORT_SERVER);
		} catch (RemoteException e) {
			consumer.accept("[ServerModel.init()]: Exception when rebinging user " + e.getMessage());
			e.printStackTrace();
		}

		lobby = user.createChatRoom("LobbyRoom");
		lobbyCr = new ServerCRManager(lobby, true);

		IChatRoom team1 = new ServerChatRoom("Team1");
		ServerCRManager cr1 = new ServerCRManager(team1, false);
		IChatRoom team2 = new ServerChatRoom("Team2");
		ServerCRManager cr2 = new ServerCRManager(team2, false);
		rooms.add(team1);
		team1room = team1;
		rooms.add(team2);
		team2room = team2;
		managers.add(cr1);
		managers.add(cr2);
	}

	/**
	 * Start the game
	 */
	public void startGame() {
		for (ServerCRManager cr : managers) {
			cr.broadCast(DemoGameMsg.class, new DemoGameMsg());
		}
	}

	/**
	 * Send the sengoku game to all players
	 */
	public void sendSengokuGame() {
		System.out.println("[ServerModel.sendSengokuGame()] --- ");
		// 1. first start the game model on server side.
		_sengokuModel = new ServerGameModel(team1room, team2room, new IServerGameModel2ServerModelAdapter() {
			@Override
			public <T extends ICRMessageType> void broadcast(Class<T> id, T data) {
				for (ServerCRManager cr : managers) {
					cr.broadCast(id, data);
				}
			}

			@Override
			public UUID getUserID() {
				try {
					return user.getUUID();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public <T extends ICRMessageType> void broadCastNonUniform(Class<T> id, HashMap<UUID, T> uuidDataPairs) {
				for (ServerCRManager cr : managers) {
					cr.broadCastNonUniform(id, uuidDataPairs);
				}
			}
		});
		// 2. send game msg to all players;
		for (ServerCRManager cr : managers) {
			cr.broadCast(SengokuGameMsg.class, new SengokuGameMsg());
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("<<ServerModel.sendSengokuGame()>> interrupted exception when has the thread sleeping");
			e.printStackTrace();
		}
		_sengokuModel.notifyResult4Turn();
	}

	/**
	 * Get a list of chat rooms
	 * @return a list of chat rooms
	 */
	public List<IChatRoom> getChatRooms() {
		return rooms;
	}

	/**
	 * Send choose team gui
	 */
	public void sendTeams() {
		System.out.println("[ServerModel.sendTeams()] --- ");
		lobbyCr.broadCast(ChooseTeamMsg.class, new ChooseTeamMsg());
	}
}
