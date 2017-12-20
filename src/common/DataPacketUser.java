package common;

import provided.datapacket.DataPacket;

/**
 * A more concrete data packet that holds a generic type of data, 
 * which will be used for transmitting messages between users.
 * @param <T> The type of the data being held.  T must be IUserMessageType. 
 */
public class DataPacketUser<T extends IUserMessageType> extends DataPacket<T, IUser> {

	private static final long serialVersionUID = 7763465178724651887L;

	/**
	 * The constructor for a data packet
	 * <pre>
	 * DataPacketUser&lt;MyData&gt; dpUser = new DataPacketUser&lt;MyData&gt;(MyData.class, myData, sender)
	 * </pre>
	 * @param c Must be T.class where T is the data type being used.
	 * @param data The data to be held in the data packet
	 * @param sender The sender of this data packet
	 */
	public DataPacketUser(Class<T> c, T data, IUser sender) {
		super(c, data, sender);
	}

}