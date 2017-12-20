package yt30_lp28.chatapp.mvc.model;

import javax.swing.ImageIcon;
import common.DataPacketCR;
import common.IChatRoom;
import common.datatype.chatroom.IRemoveReceiverType;
import yt30_lp28.chatapp.impl.MyComponent;
import yt30_lp28.chatapp.impl.MyImageIcon;
import yt30_lp28.chatapp.impl.MyString;
import yt30_lp28.chatapp.impl.Receiver;
import yt30_lp28.chatapp.impl.RemoveReceiverType;
import yt30_lp28.chatapp.impl.User;

/**
 * MiniModel, related to only one chat room
 */
public class MiniModel {
	private IChatRoom chatRoom;
	private User user;
	private Receiver receiver;

	/**
	 * Constructor for MiniModel
	 * @param chatRoom the corresponding chat room
	 * @param user local user
	 * @param receiver local receiver corresponded to the chat room
	 */
	public MiniModel(IChatRoom chatRoom, User user, Receiver receiver) {
		this.chatRoom = chatRoom;
		this.user = user;
		this.receiver = receiver;
	}

	/**
	 * Leave the chat room
	 */
	public void leave() {
		user.leaveChatRoom(chatRoom);
		chatRoom.removeIReceiverStub(receiver);
		chatRoom.sendPacket(new DataPacketCR<IRemoveReceiverType>(IRemoveReceiverType.class,
				new RemoveReceiverType(receiver), receiver));
		//		
		//		for (IReceiver receiverStub : chatRoom.getIReceiverStubs())
		//			try {
		//				if (receiverStub.getUserStub().getUUID().equals(user.getUUID())) {
		//					chatRoom.removeIReceiverStub(receiver);
		//					chatRoom.sendPacket(new DataPacketCR<IRemoveReceiverType>(IRemoveReceiverType.class, 
		//							new RemoveReceiverType(receiver), receiver));
		//					return ;
		//				}
		//			} catch (RemoteException e) {
		//				e.printStackTrace();
		//			}
	}

	/**
	 * Send a message in the chat room
	 * @param msg the message to send
	 */
	public void sendMsg(String msg) {
		DataPacketCR<MyString> data = new DataPacketCR<MyString>(MyString.class, new MyString(msg), receiver);
		receiver.sendPacket(data, chatRoom);
	}

	/**
	 * Send a image in the chat room
	 * @param imageIcon the image to send
	 */
	public void sendImage(ImageIcon imageIcon) {
		DataPacketCR<MyImageIcon> data = new DataPacketCR<MyImageIcon>(MyImageIcon.class, new MyImageIcon(imageIcon),
				receiver);
		receiver.sendPacket(data, chatRoom);
	}

	/**
	 * Send a component in a chat room
	 */
	public void sendComp() {
		DataPacketCR<MyComponent> comp = new DataPacketCR<MyComponent>(MyComponent.class, new MyComponent(), receiver);
		receiver.sendPacket(comp, chatRoom);
	}
}
