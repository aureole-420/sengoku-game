package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

/**
 * IUser interface defines a set of operations of a user in our chatApp
 * IUser is NOT transmitted to anyone. ONLY the STUB of this Remote object is ever transmitted.
 */
public interface IUser extends Remote {	
	/**
	 * The name the IUser object will be bound to in the RMI Registry
	 */
	public static final String BOUND_NAME = "USER";

	/**
	 * Get the name of the user.
	 * @return The name of the user
	 * @throws RemoteException if a network error occurs
	 */
	public String getName() throws RemoteException;
	
	/**
	 * Get UUid of the user, which is a unique identifier for the user.
	 * The UUID value of the user must equals the UUID value of all receivers belonging to this user. 
	 * @return uuid of the user
	 * @throws RemoteException if a network error occurs
	 */
	public UUID getUUID() throws RemoteException;
	
	/**
	 * Get all chat rooms the user has joined.
	 * @return All chat rooms the user has joined
	 * @throws RemoteException if a network error occurs
	 */
	public Collection<IChatRoom> getChatRooms() throws RemoteException;
	
	/**
	 * A method to be called by remote user for auto-connect-back
	 * @param userStub A remote user stub
	 * @throws RemoteException if a network error occurs 
	 */
	public void connect(IUser userStub) throws RemoteException;
	
	/**
	 * Receive a data packet
	 * Note that the data packet type and visitor type are both different from the chat room level communication.
	 * @param data the received data packet
	 * @throws RemoteException if a network error occurs
	 * @param <T> The type of the data being held.
	 */
	public <T extends IUserMessageType> void receive(DataPacketUser<T> data) throws RemoteException;
}
