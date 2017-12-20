package yt30_lp28.server.impl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import common.DataPacketCR;
import common.ICRMessageType;
import common.IReceiver;
import yt30_lp28.chatapp.impl.ChatRoom;

/**
 * Chatroom implementation in the server side
 */
public class ServerChatRoom extends ChatRoom {

	private static final long serialVersionUID = -6319324100884338117L;

	/**
	 * Constructor for ServerChatRoom, takes the name of the chat room as the parameter
	 * @param name the name of the chat room
	 */
	public ServerChatRoom(String name) {
		super(name);
	}

	/**
	 * In contrast to sendPacket() method which send datapacket uniformly, i.e., the same data packet to every one.
	 * This method send datapacket non-uniformly, i.e., different message to different user.
	 * @param dataPackets
	 */
	public <T extends ICRMessageType> void sendNonUnifrom(HashMap<UUID, DataPacketCR<T>> dataPackets) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (Iterator<IReceiver> iterator = receiverStubs.iterator(); iterator.hasNext();) {
					IReceiver receiver = iterator.next();
					try {
						if (dataPackets.containsKey(receiver.getUUID())) {
							receiver.receive(dataPackets.get(receiver.getUUID()));
						}
					} catch (RemoteException e) {
						e.printStackTrace();
						System.out.println("Receiver is out of reach ... ");
						iterator.remove();
						System.out.println("Done with removing this receiver from the chat room's receiver list ... ");
					}
				}
			}
		}) {
		}.start();
	}

}
