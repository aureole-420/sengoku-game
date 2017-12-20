package yt30_lp28.server.impl;

import java.rmi.RemoteException;
import java.util.List;

import common.*;
import common.datatype.chatroom.IAddReceiverType;
import common.datatype.chatroom.ICRInstallCmdType;
import common.datatype.user.IInvitationType;
import yt30_lp28.sengokugame.serverSide.InterTeamStringBackToServerMsg;
import yt30_lp28.sengokugame.serverSide.InterTeamStringCmd;
import yt30_lp28.sengokugame.serverSide.InterTeamStringMsg;
import yt30_lp28.sengokugame.serverSide.IntraTeamStringCmd;
import yt30_lp28.sengokugame.serverSide.IntraTeamStringMsg;
import yt30_lp28.sengokugame.serverSide.RoundDecisionCmd;
import yt30_lp28.sengokugame.serverSide.RoundDecisionMsg;
import yt30_lp28.sengokugame.serverSide.RoundResultCmd;
import yt30_lp28.sengokugame.serverSide.RoundResultMsg;
import yt30_lp28.sengokugame.serverSide.SengokuGameCmd;
import yt30_lp28.sengokugame.serverSide.SengokuGameEndCmd;
import yt30_lp28.sengokugame.serverSide.SengokuGameEndMsg;
import yt30_lp28.sengokugame.serverSide.SengokuGameMsg;
import yt30_lp28.chatapp.impl.*;
import yt30_lp28.server.cmd.ChooseTeamCmd;
import yt30_lp28.server.cmd.ChooseTeamMsg;
import yt30_lp28.server.cmd.Invitation;
import yt30_lp28.server.cmd.SelectTeam;
import yt30_lp28.server.demogame.DemoGameCmd;
import yt30_lp28.server.demogame.DemoGameMsg;
import yt30_lp28.server.demogame.GuessInt;
import yt30_lp28.server.demogame.GuessResult;
import yt30_lp28.server.demogame.GuessResultCmd;

/**
 * Implementation of IReceiver, with more local methods
 */
public class ServerReceiver extends Receiver {
	private ServerUser user;
	private IServerCRCmd2ModelAdapter cmd2ModelAdpt;

	/**
	 * Constructor for Receiver
	 * @param chatRoom the related chat room 
	 * @param user 
	 * @param userStub the related user's stub
	 * @param cmd2ModelAdpt adapter for command talking with local model
	 */
	public ServerReceiver(IChatRoom chatRoom, ServerUser user, IUser userStub,
			IServerCRCmd2ModelAdapter cmd2ModelAdpt) {
		super(chatRoom, userStub, cmd2ModelAdpt);
		this.user = user;
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

	/**
	 * 
	 */
	public void init() {
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
				if (room.addIReceiverStub(remoteReceiver)) {
					cmd2ModelAdpt.addReceiver(host.getData().getReceiverStub());

					//Send cmd for supporting chattring in the game
					try {
						remoteReceiver
								.receive(
										new DataPacketCR<ICRInstallCmdType>(ICRInstallCmdType.class,
												new InstallCmdType(IntraTeamStringMsg.class,
														new IntraTeamStringCmd(cmd2ModelAdpt, user.getUUID())),
												receiverStub));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					room.sendPacket(new DataPacketCR<IAddReceiverType>(IAddReceiverType.class,
							new AddReceiverType(remoteReceiver), receiverStub));
				}
				if (room.addIReceiverStub(host.getSender())) {
					cmd2ModelAdpt.addReceiver(host.getSender());
				}
				return null;
			}
		});

		algo.setCmd(ChooseTeamMsg.class,
				new ChooseTeamCmd(ServerReceiver.this, cmd2ModelAdpt.getChatRooms(), cmd2ModelAdpt));
		algo.setCmd(SelectTeam.class, new DataPacketCRAlgoCmd<SelectTeam>() {
			private static final long serialVersionUID = 7710578839212535098L;

			@Override
			public String apply(Class<?> index, DataPacketCR<SelectTeam> host, String... params) {
				IChatRoom cr = host.getData().getRoom();
				for (IChatRoom room : cmd2ModelAdpt.getChatRooms()) {
					if (room.getUUID().equals(cr.getUUID())) {
						try {
							host.getSender().getUserStub().receive(new DataPacketUser<IInvitationType>(
									IInvitationType.class, new Invitation(room), userStub));
							break;
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				//				try {
				//					for (IUser u : user.getConnectedUsers()) {
				//						if (u.getUUID().equals(host.getSender().getUUID())) {
				//							u.receive(new DataPacketUser<IInvitationType>(IInvitationType.class, new Invitation(cr), userStub));
				//							break;
				//						}
				//					}
				//				} catch (RemoteException e) {
				//					e.printStackTrace();
				//				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
			}
		});

		algo.setCmd(DemoGameMsg.class, new DemoGameCmd(cmd2ModelAdpt, receiverStub));
		algo.setCmd(GuessResult.class, new GuessResultCmd(receiverStub));

		algo.setCmd(RoundDecisionMsg.class, new RoundDecisionCmd(cmd2ModelAdpt));
		try {
			algo.setCmd(SengokuGameMsg.class, new SengokuGameCmd(receiverStub, cmd2ModelAdpt, user.getUUID()));
			algo.setCmd(RoundResultMsg.class, new RoundResultCmd(cmd2ModelAdpt, user.getUUID()));
			algo.setCmd(InterTeamStringMsg.class, new InterTeamStringCmd(cmd2ModelAdpt, user.getUUID()));
			algo.setCmd(IntraTeamStringMsg.class, new IntraTeamStringCmd(cmd2ModelAdpt, user.getUUID()));
			algo.setCmd(SengokuGameEndMsg.class, new SengokuGameEndCmd(cmd2ModelAdpt, user.getUUID()));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		algo.setCmd(InterTeamStringBackToServerMsg.class, new DataPacketCRAlgoCmd<InterTeamStringBackToServerMsg>() {
			private static final long serialVersionUID = 3606123286676229012L;

			@Override
			public String apply(Class<?> index, DataPacketCR<InterTeamStringBackToServerMsg> host, String... params) {
				// TODO Auto-generated method stub
				List<IChatRoom> rooms = cmd2ModelAdpt.getChatRooms();
				if (rooms == null) {
					return null;
				}
				System.out.println("[SererReceiver InterTeamStringBackToServerMsg] ... ");
				for (IChatRoom room : rooms) {
					room.sendPacket(new DataPacketCR<InterTeamStringMsg>(InterTeamStringMsg.class,
							new InterTeamStringMsg(host.getData().getData()), receiverStub));
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub

			}
		});

		algo.setCmd(GuessInt.class, new DataPacketCRAlgoCmd<GuessInt>() {
			private static final long serialVersionUID = 8499017522980779065L;

			@Override
			public String apply(Class<?> index, DataPacketCR<GuessInt> host, String... params) {
				// TODO Auto-generated method stub
				if (host.getData().getGuess() > 50) {
					room.sendPacket(new DataPacketCR<GuessResult>(GuessResult.class,
							new GuessResult(host.getData() + " Too big!"), receiverStub));
				} else if (host.getData().getGuess() < 50) {
					room.sendPacket(new DataPacketCR<GuessResult>(GuessResult.class,
							new GuessResult(host.getData() + " Too small!"), receiverStub));
				} else {
					room.sendPacket(new DataPacketCR<GuessResult>(GuessResult.class,
							new GuessResult(host.getData() + " Bingo! You have won the game!"), receiverStub));
					for (IChatRoom others : cmd2ModelAdpt.getChatRooms()) {
						if (!others.getUUID().equals(room.getUUID())) {
							others.sendPacket(new DataPacketCR<GuessResult>(GuessResult.class,
									new GuessResult("Sad! You lose the game!"), receiverStub));
						}
					}
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {

			}
		});
	}

	@Override
	public <T extends ICRMessageType> void receive(DataPacketCR<T> data) throws RemoteException {
		if (data.getSender().getUUID().equals(receiverStub.getUUID())) {
			return;
		}
		data.execute(algo);
	}
}
