package common;

import provided.datapacket.ADataPacketAlgoCmd;

/**
 * Specialized data packet processing command that uses DataPacketCR&lt;T&gt; and ICRCmd2ModelAdapters.
 * Here we specify return data type is String, and the input parameter type is also String.
 * @param <T>  The data in the data packet
 */
public abstract class DataPacketCRAlgoCmd<T extends ICRMessageType>
		extends ADataPacketAlgoCmd<String, T, String, ICRCmd2ModelAdapter, DataPacketCR<T>> {
	private static final long serialVersionUID = 3493108470390456052L;
}
