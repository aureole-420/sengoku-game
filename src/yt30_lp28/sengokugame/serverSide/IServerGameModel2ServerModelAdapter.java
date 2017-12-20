package yt30_lp28.sengokugame.serverSide;

import java.util.HashMap;
import java.util.UUID;

import common.ICRMessageType;

/**
 * This adapter allows the game model (on server side) to call server's method.
 * @author yt30, lp28
 *
 */
public interface IServerGameModel2ServerModelAdapter {
	/**
	 * Broadcast message to all game player;
	 * @param id the index of message type
	 * @param data the message to transmit
	 * @T 
	 */
	public <T extends ICRMessageType> void broadcast(Class<T> id, T data);

	/**
	 * Getter for User's UUID (actually the server'sUUID)
	 * @return the server's User UUID
	 */
	public UUID getUserID();

	/**
	 * broadcat message to all game player, but with each player receiving unique messages;
	 * @param id the index of message type;
	 * @param uuidDataPairs a HashMap of data to be transmitted 
	 */
	public <T extends ICRMessageType> void broadCastNonUniform(Class<T> id, HashMap<UUID, T> uuidDataPairs);
}
