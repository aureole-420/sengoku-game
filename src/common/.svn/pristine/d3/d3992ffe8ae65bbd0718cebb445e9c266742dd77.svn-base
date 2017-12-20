package common;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
/**
 * A generalized chat room interface that defines a set of operations of a chat room.
 * A chat room is a FULLY SERIALIZABLE object that is wholly transmitted from one machine to another machine
 * A chat room holds a set of IReceiver stubs.
 */
public interface IChatRoom extends Serializable{
	
	/**
	 * Get the name of the chat room
	 * @return The name of the chat room
	 */
	public String getName();
	
	/**
	 * Get UUID of this chat room, which is an unique identifier for this chat room
	 * @return UUID of this chat room
	 */
	public UUID getUUID();
	
	/**
	 * Get all receivers' stubs in the chat room at the moment the method is called
	 * @return All the receivers' stubs in the chat room at the moment. 
	 */
	public Collection<IReceiver> getIReceiverStubs();
	
	/**
	 * Send a data packet to everyone in the chat room asynchronously.
	 * This method should send the data packet asynchronously,
	 * i.e. delegates the sending process into a new thread.
	 * The possible status will be returned in an asynchronous way, which will be processed by the receiver.
	 * @param data The data packet to be sent
	 * @param <T> The type of the data being held.
	 */
	public <T extends ICRMessageType> void sendPacket(DataPacketCR<T> data);
	
	/**
	 * Add a receiver in the local chat room without interacting with remote peers
	 * @param receiver the receiver to add
	 * @return False if the receiver is already in the chat room; True if successfully add the receiver
	 */
	public boolean addIReceiverStub(IReceiver receiver);
	
	/**
	 * Remove a receiver in the local chat room without interacting with remote peers
	 * @param receiver the receiver to remove
	 * @return False if the receiver is actually not in the chat room; True if successfully remove the receiver
	 */
	public boolean removeIReceiverStub(IReceiver receiver);
}
