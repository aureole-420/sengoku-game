package yt30_lp28.server.impl;

import java.rmi.RemoteException;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IChatRoom;
import common.IReceiver;
import common.IUser;
import common.datatype.chatroom.IAddReceiverType;
import yt30_lp28.chatapp.impl.AddReceiverType;
import yt30_lp28.server.cmd.ChooseTeamMsg;

/**
 * The server receiver type in the lobby.
 */
public class LobbyReceiver extends ServerReceiver {
	private IServerCRCmd2ModelAdapter cmd2ModelAdpt;

	/**
	 * Constructor for LobbyReceiver
	 * @param chatRoom0 the chat room object
	 * @param user0 the use object
	 * @param userStub the user stub
	 * @param cmd2ModelAdpt0 IServerCRCmd2ModelAdapter object
	 */
	public LobbyReceiver(IChatRoom chatRoom0, ServerUser user0, IUser userStub,
			IServerCRCmd2ModelAdapter cmd2ModelAdpt0) {
		super(chatRoom0, user0, userStub, cmd2ModelAdpt0);
		this.cmd2ModelAdpt = cmd2ModelAdpt0;
	}

	@Override
	public void init() {
		super.init();
		algo.setCmd(IAddReceiverType.class, new DataPacketCRAlgoCmd<IAddReceiverType>() {
			private static final long serialVersionUID = -899370427151894987L;

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
					room.sendPacket(new DataPacketCR<IAddReceiverType>(IAddReceiverType.class,
							new AddReceiverType(remoteReceiver), receiverStub));
					try {
						cmd2ModelAdpt.sendTo(host.getSender(), ChooseTeamMsg.class, new ChooseTeamMsg());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (room.addIReceiverStub(host.getSender())) {
					cmd2ModelAdpt.addReceiver(host.getSender());
					try {
						cmd2ModelAdpt.sendTo(host.getSender(), ChooseTeamMsg.class, new ChooseTeamMsg());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}

		});
	}
}