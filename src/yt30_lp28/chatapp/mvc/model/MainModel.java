package yt30_lp28.chatapp.mvc.model;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import common.*;
import common.datatype.chatroom.IAddReceiverType;
import common.datatype.user.IQuitType;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;
import yt30_lp28.chatapp.impl.AddReceiverType;
import yt30_lp28.chatapp.impl.ILocalCmd2ModelAdapter;
import yt30_lp28.chatapp.impl.IUser2ModelAdapter;
import yt30_lp28.chatapp.impl.MyComponent;
import yt30_lp28.chatapp.impl.MyString;
import yt30_lp28.chatapp.impl.ProxyUser;
import yt30_lp28.chatapp.impl.Receiver;
import yt30_lp28.chatapp.impl.User;
import yt30_lp28.chatapp.impl.user.cmd.QuitType;

/**
 * Main mvc model
 */
public class MainModel {
	private List<MiniMVCAdapter<ProxyUser>> miniModels = new LinkedList<MiniMVCAdapter<ProxyUser>>();
	private MainModel2ViewAdapter mainModel2ViewAdpt;
	private Set<ProxyUser> connectedUsers = new HashSet<ProxyUser>();
	private IMixedDataDictionary dict = new MixedDataDictionary();
	private Consumer<String> consumer;
	private IRMIUtils rmiUtils;
	private Registry registry = null;
	private User user;
	private IUser userStub;
	private final int boundport = IRMI_Defs.STUB_PORT_CLIENT;
	private int receivePort = IRMI_Defs.STUB_PORT_EXTRA;
	private String name = "Default Name";

	/**
	 * Constructor for MainModel
	 * @param mainModel2ViewAdpt adapter talking to main view
	 */
	public MainModel(MainModel2ViewAdapter mainModel2ViewAdpt) {
		this.mainModel2ViewAdpt = mainModel2ViewAdpt;
		consumer = new Consumer<String>() {
			@Override
			public void accept(String t) {
				mainModel2ViewAdpt.append(t);
			}
		};
		rmiUtils = new RMIUtils(consumer);
	}

	/**
	 * Start the model
	 */
	public void start() {
		String addr = "127.0.0.1";
		try {
			addr = rmiUtils.getLocalAddress();
		} catch (SocketException | UnknownHostException e) {
			consumer.accept("[MainModel.start()] " + e);
			e.printStackTrace();
		}
		mainModel2ViewAdpt.setServerName(addr);
	}

	/**
	 * Start the RMI server
	 * @param username the name of the user
	 * @param servername server's name (local ip address)
	 */
	public void startServer(String username, String servername) {
		this.name = username;
		rmiUtils.startRMI(IRMI_Defs.CLASS_SERVER_PORT_CLIENT);
		String name = username + "@" + servername;
		user = new User(name, new IUser2ModelAdapter() {
			@Override
			public void addConnected(IUser userStub) {
				ProxyUser proxy = new ProxyUser(userStub);
				mainModel2ViewAdpt.addConnected(proxy);
				connectedUsers.add(new ProxyUser(proxy));
			}

			@Override
			public void joinChatRoom(IChatRoom room) {
				new ChatRoomManager(room);
			}

			@Override
			public void appendMsg(String text, String label) {
				mainModel2ViewAdpt.appendUserMsg(text, label);
			}

			@Override
			public void buildScrollableComponent(IComponentFactory fac, String label) {
				mainModel2ViewAdpt.addScrollComp(fac.makeComponent(), label);
			}

			@Override
			public void buildNonScrollableComponent(IComponentFactory fac, String label) {
				mainModel2ViewAdpt.addNonScrollComp(fac.makeComponent(), label);
			}

			@Override
			public <T> T put(MixedDataKey<T> key, T value) {
				return dict.put(key, value);
			}

			@Override
			public <T> T get(MixedDataKey<T> key) {
				return dict.get(key);
			}

			@Override
			public <T extends IUserMessageType> void sendTo(IUser target, Class<T> id, T data) {
				try {
					target.receive(new DataPacketUser<T>(id, data, userStub));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public void removeConnected(IUser userStub) {
				mainModel2ViewAdpt.removeConnected(new ProxyUser(userStub));
			}
		});
		consumer.accept("[MainModel.startServer()]: You have logged in as " + name);
		registry = rmiUtils.getLocalRegistry();
		try {
			userStub = (IUser) UnicastRemoteObject.exportObject(user, boundport);
			user.setSelfStub(userStub);
		} catch (RemoteException e) {
			consumer.accept("[MainModel.startServer()]: Exception when export object " + e.getMessage());
			e.printStackTrace();
		}
		try {
			registry.rebind(IUser.BOUND_NAME, userStub);
			mainModel2ViewAdpt.addConnected(new ProxyUser(userStub));
			consumer.accept(
					"[MainModel.startServer()]: Registry successfully bind " + name + " To " + IUser.BOUND_NAME);
		} catch (RemoteException e) {
			consumer.accept("[MainModel.startServer()]: Exception when rebinging user " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Internal class for managing chat rooms.
	 */
	private class ChatRoomManager {
		Receiver receiver;
		MiniMVCAdapter<ProxyUser> miniAdpt;
		IReceiver receiverStub;

		ChatRoomManager(IChatRoom room) {
			this.receiver = new Receiver(room, userStub, new ILocalCmd2ModelAdapter() {
				@Override
				public void addReceiver(IReceiver receiverStub) {
					try {
						miniAdpt.addItem(new ProxyUser(receiverStub.getUserStub()));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void removeReceiver(IReceiver receiverStub) {
					try {
						miniAdpt.removeItem(new ProxyUser(receiverStub.getUserStub()));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void appendMsg(String text, String name) {
					miniAdpt.append("[" + name + "]: " + text);
				}

				@Override
				public void buildScrollableComponent(IComponentFactory fac, String label) {
					miniAdpt.addScrollableComponent(fac.makeComponent(), label);
				}

				@Override
				public void buildNonScrollableComponent(IComponentFactory fac, String label) {
					miniAdpt.addNonScrollableComponent(fac.makeComponent(), label);
				}

				@Override
				public <T> T put(MixedDataKey<T> key, T value) {
					return dict.put(key, value);
				}

				@Override
				public <T> T get(MixedDataKey<T> key) {
					return dict.get(key);
				}

				@Override
				public <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data) {
					room.sendPacket(new DataPacketCR<T>(id, data, receiverStub));
				}

				@Override
				public <T extends ICRMessageType> void sendTo(IReceiver target, Class<T> id, T data) {
					try {
						target.receive(new DataPacketCR<T>(id, data, receiverStub));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}

				@Override
				public String getName() {
					try {
						return user.getName();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					return name;
				}
			});
			try {
				receiverStub = (IReceiver) UnicastRemoteObject.exportObject(receiver, receivePort);
				receiver.setReceiverStub(receiverStub);
				receivePort += 2;

				consumer.accept("Create a new chat room " + room.getName());
				this.miniAdpt = mainModel2ViewAdpt.makeMiniMVC(room, user, receiver);
				room.sendPacket(new DataPacketCR<IAddReceiverType>(IAddReceiverType.class,
						new AddReceiverType(receiverStub), receiverStub));
				miniModels.add(this.miniAdpt);
				room.addIReceiverStub(receiverStub);
				miniAdpt.addItem(new ProxyUser(userStub));
			} catch (RemoteException e) {
				e.printStackTrace();
				return;
			}
		}

		ChatRoomManager(String name) {
			this(user.createChatRoom(name));
		}
	}

	/**
	 * Create a chat room with a given name
	 * @param name name of the chat room
	 */
	public void createChatRoom(String name) {
		new ChatRoomManager(name);
	}

	/**
	 * Connect to a remote host
	 * @param remoteHost address of remote host
	 */
	public void connectToPeer(String remoteHost) {
		if (remoteHost.trim().equals("")) {
			consumer.accept("[MainModel.connectToPeer()]: Illegal argument ...");
			return;
		}

		consumer.accept("Grabbing the registry from " + remoteHost);
		Registry reg = rmiUtils.getRemoteRegistry(remoteHost);
		consumer.accept("Found the remote registry " + reg);
		IUser remoteUser = null;
		try {
			remoteUser = (IUser) reg.lookup(IUser.BOUND_NAME);

			if (remoteUser == null) {
				System.err.println("[MainModel.connecttoPeer()]: Remote user is null");
				return;
			}
			if (remoteUser.equals(userStub)) {
				consumer.accept("[MainModel.connectTo()] you are connected to localhost");
				return;
			}
			if (connectedUsers.add(new ProxyUser(remoteUser))) {
				remoteUser.connect(userStub);
				mainModel2ViewAdpt.addConnected(new ProxyUser(remoteUser));
			} else {
				consumer.accept("[MainModel.connectTo()] Already connectecd to the user ... ");
			}

		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			System.err.println("RemoteException ... Connect to remote registry failed");
			System.err.println("Check your input and try again ...");
		}
	}

	/**
	 * Stop the model, exit the application
	 */
	public void stop() {
		consumer.accept("[MainModel.stop()]: Quit all chat rooms and disconnected all users");
		//Quit all chat rooms
		for (MiniMVCAdapter<ProxyUser> miniModel : miniModels)
			miniModel.quit();

		//Send IQuitType messages to connected users
		for (IUser remotePeer : connectedUsers) {
			try {
				remotePeer.receive(new DataPacketUser<IQuitType>(IQuitType.class, new QuitType(userStub), userStub));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		try {
			registry.unbind(IUser.BOUND_NAME);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			System.exit(-1); // exit the program.
		}
		rmiUtils.stopRMI();
		consumer.accept("[MainModel.stop()]: successfully unbind " + IUser.BOUND_NAME);
		System.exit(0);
	}

	/**
	 * Request chat room list from a remote user
	 * @param remoteUser the remote user
	 */
	public void request(ProxyUser remoteUser) {
		consumer.accept("Request chat rooms from " + remoteUser);
		Iterable<IChatRoom> rooms = null;
		try {
			rooms = remoteUser.getChatRooms();
		} catch (RemoteException e) {
			consumer.accept("Exception when getting chat rooms from the user " + remoteUser);
			e.printStackTrace();
			return;
		}

		ArrayList<IChatRoom> tempRooms = new ArrayList<IChatRoom>();
		rooms.forEach(tempRooms::add);
		Object[] chatRooms = tempRooms.toArray();

		Object choose = JOptionPane.showInputDialog(null, "Choose one chat room", "Request chat room",
				JOptionPane.INFORMATION_MESSAGE, null, chatRooms, chatRooms.length > 0 ? chatRooms[0] : null);
		if (choose == null) {
			consumer.accept("No Chat Room Selected");
			return;
		}
		IChatRoom cr = (IChatRoom) choose;
		if (user.joinChatRoom(cr)) {
			new ChatRoomManager(cr);
			mainModel2ViewAdpt.append("Join a new chat room " + cr.getName() + " ... ");
		} else {
			mainModel2ViewAdpt.append("Already in the chat room " + cr.getName() + " ... ");
		}
	}

	/**
	 * Get stub of the local user
	 * @return stub of the local user
	 */
	public IUser getUserStub() {
		return userStub;
	}

	/**
	 * Send a message to a target user
	 * @param text the message text
	 * @param target the target user
	 */
	public void sendMsg(String text, ProxyUser target) {
		try {
			target.receive(new DataPacketUser<MyString>(MyString.class, new MyString(text), userStub));
		} catch (RemoteException e) {
			e.printStackTrace();
			try {
				System.err.println("[MainModel.sendMsg()]: remote user is out of reach ..." + target.getName());
				connectedUsers.remove(target);
				mainModel2ViewAdpt.removeConnected(target);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * @param target
	 */
	public void sendComp(ProxyUser target) {
		try {
			target.receive(new DataPacketUser<MyComponent>(MyComponent.class, new MyComponent(), userStub));
		} catch (RemoteException e) {
			e.printStackTrace();
			try {
				System.err.println("[MainModel.sendComp()]: remote user is out of reach ..." + target.getName());
				connectedUsers.remove(target);
				mainModel2ViewAdpt.removeConnected(target);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
}
