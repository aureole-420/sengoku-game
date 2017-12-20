package yt30_lp28.chatapp.impl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import common.*;

/**
 * ChatRoom defines a set of operations of a chat room, implement interface IChatRoom
 */
public class ChatRoom implements IChatRoom {
	protected static final long serialVersionUID = -5416342672425833835L;
	protected String name;
	protected Set<IReceiver> receiverStubs;
	protected UUID id;

	/**
	 * Constructor for ChatRoom, create a chat room with a given name
	 * @param name the name of the chat room
	 */
	public ChatRoom(String name) {
		this.name = name;
		receiverStubs = Collections.newSetFromMap(new ConcurrentHashMap<IReceiver, Boolean>());
		id = UUID.randomUUID();
	}

	@Override
	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public Collection<IReceiver> getIReceiverStubs() {
		return receiverStubs;
	}

	@Override
	public <T extends ICRMessageType> void sendPacket(DataPacketCR<T> data) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (Iterator<IReceiver> iterator = receiverStubs.iterator(); iterator.hasNext();) {
					IReceiver receiver = iterator.next();
					try {
						receiver.receive(data);
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

	@Override
	public boolean addIReceiverStub(IReceiver receiver) {
		for (IReceiver r : receiverStubs) {
			try {
				if (r.getUUID().equals(receiver.getUUID()))
					return false;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return receiverStubs.add(receiver);
	}

	@Override
	public boolean removeIReceiverStub(IReceiver receiver) {
		return receiverStubs.remove(receiver);
	}

	public boolean equals(Object other) {
		if (other instanceof ChatRoom) {
			return id.equals(((IChatRoom) other).getUUID());
		}
		return false;
	}

	public int hashCode() {
		return id.hashCode();
	}
}