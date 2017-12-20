package yt30_lp28.server.impl;

import java.util.List;

import common.ICRMessageType;
import common.IChatRoom;
import common.IComponentFactory;
import common.IReceiver;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;

/**
 * Implementation of IServerCRCmd2ModelAdapter
 */
public abstract class ServerCRCmd2ModelAdapter implements IServerCRCmd2ModelAdapter {

	private IChatRoom room;
	private IMixedDataDictionary dict = new MixedDataDictionary();

	/**
	 * @param room
	 */
	public ServerCRCmd2ModelAdapter(IChatRoom room) {
		// TODO Auto-generated constructor stub
		this.room = room;
	}

	@Override
	public void addReceiver(IReceiver receiverStub) {
		// TODO Auto-generated method stub
		room.addIReceiverStub(receiverStub);
	}

	@Override
	public void removeReceiver(IReceiver receiverStub) {
		// TODO Auto-generated method stub
		room.removeIReceiverStub(receiverStub);
	}

	@Override
	public <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data) {
		// TODO Auto-generated method stub
		System.out.println("[ServerCRCmd2ModelAdapter.sendToChatRoom]--- " + data.toString());
	}

	@Override
	public void appendMsg(String text, String name) {
		// TODO Auto-generated method stub
		System.out.println("[ServerCRCmd2ModelAdapter.appendMsg]--- " + text + " from " + name);
	}

	@Override
	public void buildScrollableComponent(IComponentFactory fac, String label) {
		// TODO Auto-generated method stub
		System.out.println("[ServerCRCmd2ModelAdapter.buildScrollableComponent]--- label " + label);

	}

	@Override
	public void buildNonScrollableComponent(IComponentFactory fac, String label) {
		// TODO Auto-generated method stub
		System.out.println("[ServerCRCmd2ModelAdapter.buildNonScrollableComponent]--- label " + label);

	}

	@Override
	public <T> T put(MixedDataKey<T> key, T value) {
		// TODO Auto-generated method stub
		return dict.put(key, value);
	}

	@Override
	public <T> T get(MixedDataKey<T> key) {
		// TODO Auto-generated method stub
		return dict.get(key);
	}

	@Override
	public abstract <T extends ICRMessageType> void sendTo(IReceiver target, Class<T> id, T data);

	@Override
	public abstract List<IChatRoom> getChatRooms();
}
