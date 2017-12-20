package common;

import provided.datapacket.ADataPacketAlgoCmd;

/**
 * Specialized data packet processing command that uses DataPacketUser&lt;T&gt; and IUserCmd2ModelAdapter.
 * Here we specify return data type is String, and the input parameter type is also String.
 * @param <T>  The data in the data packet, T must extends IUserMessageType
 */
public abstract class DataPacketUserAlgoCmd<T extends IUserMessageType>
		extends ADataPacketAlgoCmd<String, T, String, IUserCmd2ModelAdapter, DataPacketUser<T>> {

	private static final long serialVersionUID = -2208088837499391770L;

}
