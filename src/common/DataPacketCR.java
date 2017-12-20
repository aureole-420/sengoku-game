package common;

import provided.datapacket.DataPacket;

/**
 * A more concrete data packet that holds a generic type of data, 
 * which will be used for transmitting messages in chat rooms.
 * Specifying the ISender type senders of DataPacket to be IReceiver
 * @param <T> The type of the data being held.  T must be Serializable. 
 */
public class DataPacketCR<T extends ICRMessageType> extends DataPacket<T, IReceiver> {

	/**
	 * Version number for serialization
	 */
	private static final long serialVersionUID = -7174702729804925974L;

	/**
	 * The constructor for a data packet
	 * <pre>
	 * DataPacketCR&lt;MyData&gt; dpcr = new DataPacketCR&lt;MyData&gt;(MyData.class, myData, sender)
	 * </pre>
	 * @param c Must be T.class where T is the data type being used.
	 * @param data The data to be held in the data packet
	 * @param sender The sender of this data packet
	 */
	public DataPacketCR(Class<T> c, T data, IReceiver sender) {
		super(c, data, sender);
	}
}
