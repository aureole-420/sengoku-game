package yt30_lp28.chatapp.impl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.extvisitor.IExtVisitorCmd;
import common.*;
import common.datatype.IRequestCmdType;
import common.datatype.chatroom.*;

/**
 * Implementation of IReceiver, with more local methods
 */
public class Receiver implements IReceiver {

	protected IUser userStub;
	protected ILocalCmd2ModelAdapter cmd2ModelAdpt;
	protected Map<Class<?>, List<DataPacketCR<?>>> messageCache;
	protected UUID id;
	protected IReceiver receiverStub;
	protected IChatRoom room;

	protected DataPacketAlgo<String, String> algo = new DataPacketAlgo<String, String>(
			new DataPacketCRAlgoCmd<ICRMessageType>() {
				private static final long serialVersionUID = 1412561293137057537L;

				@Override
				public String apply(Class<?> id, DataPacketCR<ICRMessageType> host, String... params) {
					Object x = host.getData();
					String name = "Unknown remote name ";
					try {
						name = host.getSender().getUserStub().getName();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					cmd2ModelAdpt.appendMsg("[Algo.apply() ] Default case called. successfully process data " + x,
							name);
					List<DataPacketCR<?>> cache = messageCache.getOrDefault(id, new LinkedList<DataPacketCR<?>>());
					cache.add(host);
					messageCache.put(id, cache);
					try {
						host.getSender().receive(new DataPacketCR<IRequestCmdType>(IRequestCmdType.class,
								new RequestCmdType(id), receiverStub));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
				}
			});

	/**
	 * Constructor for Receiver
	 * @param chatRoom the related chat room 
	 * @param userStub the related user's stub
	 * @param cmd2ModelAdpt adapter for command talking with local model
	 */
	public Receiver(IChatRoom chatRoom, IUser userStub, ILocalCmd2ModelAdapter cmd2ModelAdpt) {
		this.userStub = userStub;
		this.cmd2ModelAdpt = cmd2ModelAdpt;
		this.room = chatRoom;
		try {
			this.id = userStub.getUUID();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		messageCache = new HashMap<Class<?>, List<DataPacketCR<?>>>();

		algo.setCmd(IAddReceiverType.class, new DataPacketCRAlgoCmd<IAddReceiverType>() {
			private static final long serialVersionUID = -3742339238391509912L;

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt0) {
			}

			@Override
			public String apply(Class<?> index, DataPacketCR<IAddReceiverType> host, String... params) {
				String name = "Unknown remote name ";
				try {
					name = host.getSender().getUserStub().getName();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				IReceiver remoteReceiver = host.getData().getReceiverStub();
				cmd2ModelAdpt.appendMsg("Case IAddReceiver " + host.getData(), name);
				if (chatRoom.addIReceiverStub(remoteReceiver)) {
					cmd2ModelAdpt.addReceiver(host.getData().getReceiverStub());
					chatRoom.sendPacket(new DataPacketCR<IAddReceiverType>(IAddReceiverType.class,
							new AddReceiverType(remoteReceiver), receiverStub));
				}
				if (chatRoom.addIReceiverStub(host.getSender())) {
					cmd2ModelAdpt.addReceiver(host.getSender());
				}
				return null;
			}
		});

		algo.setCmd(IRemoveReceiverType.class, new DataPacketCRAlgoCmd<IRemoveReceiverType>() {
			private static final long serialVersionUID = 8450099095941202285L;

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
			}

			@Override
			public String apply(Class<?> index, DataPacketCR<IRemoveReceiverType> host, String... params) {
				String name = "Unknown remote name ";
				try {
					name = host.getSender().getUserStub().getName();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cmd2ModelAdpt.appendMsg("Case IRemoveReceiver " + host.getData(), name);
				cmd2ModelAdpt.removeReceiver(host.getData().getReceiverStub());
				chatRoom.removeIReceiverStub(host.getData().getReceiverStub());
				return null;
			}
		});

		algo.setCmd(IRequestCmdType.class, new DataPacketCRAlgoCmd<IRequestCmdType>() {
			private static final long serialVersionUID = -7856523715319555526L;

			@Override
			public String apply(Class<?> index, DataPacketCR<IRequestCmdType> host, String... params) {
				Class<?> cmdId = host.getData().getCmdId();
				IExtVisitorCmd<String, Class<?>, String, ADataPacket> extVisitorCmd = algo.getCmd(cmdId);
				if (extVisitorCmd == null) {
					try {
						host.getSender().receive(new DataPacketCR<ICRExceptionStatusType>(ICRExceptionStatusType.class,
								new CmdNotFoundExceptionType(host, cmdId), receiverStub));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					return null;
				}
				DataPacketCRAlgoCmd<?> cmd = (DataPacketCRAlgoCmd<?>) extVisitorCmd;
				try {
					host.getSender().receive(new DataPacketCR<ICRInstallCmdType>(ICRInstallCmdType.class,
							new InstallCmdType(cmdId, cmd), receiverStub));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
			}
		});

		algo.setCmd(ICRInstallCmdType.class, new DataPacketCRAlgoCmd<ICRInstallCmdType>() {
			private static final long serialVersionUID = 4795901236158766777L;

			@Override
			public String apply(Class<?> index, DataPacketCR<ICRInstallCmdType> host, String... params) {
				Class<?> cmdId = host.getData().getCmdId();
				DataPacketCRAlgoCmd<?> cmd = host.getData().getCmd();
				if (algo.getCmd(cmdId) != null) {
					try {
						host.getSender().receive(new DataPacketCR<ICRRejectionStatusType>(ICRRejectionStatusType.class,
								new DuplicateCmdRejectionType(host, cmdId), receiverStub));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					return null;
				}
				System.out.println("install cmd: " + host.getData().getCmdId());
				try {
					System.out.println("install cmd: " + receiverStub.getUserStub().getName());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("cmd2modeladpt == null" + (cmd2ModelAdpt == null));
				cmd.setCmd2ModelAdpt(cmd2ModelAdpt);
				algo.setCmd(cmdId, cmd);
				if (messageCache.get(cmdId) == null)
					return null;
				for (DataPacketCR<?> packet : messageCache.get(cmdId))
					packet.execute(algo);
				messageCache.remove(cmdId);
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
			}
		});

		algo.setCmd(ICRExceptionStatusType.class, new DataPacketCRAlgoCmd<ICRExceptionStatusType>() {
			private static final long serialVersionUID = -4558017546995485242L;

			@Override
			public String apply(Class<?> index, DataPacketCR<ICRExceptionStatusType> host, String... params) {
				ICRExceptionStatusType exception = host.getData();
				String name = "Unknown remote name ";
				try {
					name = host.getSender().getUserStub().getName();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cmd2ModelAdpt.appendMsg("IExceptionStatusType " + exception.getFailureInfo() + "  original msg "
						+ exception.getOriginalData(), name);
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
			}
		});

		algo.setCmd(ICRRejectionStatusType.class, new DataPacketCRAlgoCmd<ICRRejectionStatusType>() {
			private static final long serialVersionUID = -6825472604505704316L;

			@Override
			public String apply(Class<?> index, DataPacketCR<ICRRejectionStatusType> host, String... params) {
				ICRRejectionStatusType rejection = host.getData();
				String name = "Unknown remote name ";
				try {
					name = host.getSender().getUserStub().getName();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cmd2ModelAdpt.appendMsg("IRejectionStatusType " + rejection.getFailureInfo() + "  original msg "
						+ rejection.getOriginalData(), name);
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
			}
		});

		algo.setCmd(ICRFailureStatusType.class, new DataPacketCRAlgoCmd<ICRFailureStatusType>() {
			private static final long serialVersionUID = -349858534319800420L;

			@Override
			public String apply(Class<?> index, DataPacketCR<ICRFailureStatusType> host, String... params) {
				ICRFailureStatusType failure = host.getData();
				String name = "Unknown remote name ";
				try {
					name = host.getSender().getUserStub().getName();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cmd2ModelAdpt.appendMsg("IFailureStatusType " + failure.getFailureInfo() + "  original msg "
						+ failure.getOriginalData(), name);
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
			}
		});

		//Customized command
		algo.setCmd(MyString.class, new StringCmd(cmd2ModelAdpt));
		algo.setCmd(MyImageIcon.class, new ImageCmd(cmd2ModelAdpt));
		algo.setCmd(MyComponent.class, new CompCmd(cmd2ModelAdpt));
		//		algo.setCmd(A.class, new SimpleGameCmd(cmd2ModelAdpt, Receiver.this));
	}

	@Override
	public <T extends ICRMessageType> void receive(DataPacketCR<T> data) throws RemoteException {
		data.execute(algo);
	}

	@Override
	public IUser getUserStub() throws RemoteException {
		return userStub;
	}

	/**
	 * Send a data packet in a chat room
	 * @param data the data packet to send
	 * @param room the chat room where the packet will be sent
	 * @param  <T> type of data packet
	 */
	public <T extends ICRMessageType> void sendPacket(DataPacketCR<T> data, IChatRoom room) {
		room.sendPacket(data);
	}

	@Override
	public UUID getUUID() throws RemoteException {
		return id;
	}

	/**
	 * Set the stub corresponding with this receiver object
	 * @param receiverStub the corresponding stub
	 */
	public void setReceiverStub(IReceiver receiverStub) {
		this.receiverStub = receiverStub;
	}

	public boolean equals(Object other) {
		if (other instanceof IReceiver) {
			try {
				return id.equals(((IReceiver) other).getUUID());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public int hashCode() {
		return id.hashCode();
	}
}
