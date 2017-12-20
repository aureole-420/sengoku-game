package yt30_lp28.chatapp.impl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

import common.DataPacketUser;
import common.IChatRoom;
import common.IUser;
import common.IUserMessageType;

/**
 * ProxyUser is a wrapper class of IUser, to support toString() and equals() method
 */
public class ProxyUser implements IUser {

	private IUser stub;
	private UUID id;
	private String name;

	/**
	 * Create a proxy user using a user stub
	 * @param user a remote user stub
	 */
	public ProxyUser(IUser user) {
		this.stub = user;
		try {
			this.name = user.getName();
			this.id = user.getUUID();
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("RemoteException occuring when creating ProxyUser ... ");
			this.name = "RemoteException name";
			this.id = UUID.randomUUID();
		}
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public Collection<IChatRoom> getChatRooms() throws RemoteException {
		return stub.getChatRooms();
	}

	@Override
	public UUID getUUID() throws RemoteException {
		return id;
	}

	@Override
	public void connect(IUser userStub) throws RemoteException {
		stub.connect(userStub);
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object other) {
		if (other instanceof ProxyUser) {
			try {
				return id.equals(((ProxyUser) other).getUUID());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public <T extends IUserMessageType> void receive(DataPacketUser<T> data) throws RemoteException {
		stub.receive(data);
	}
}
