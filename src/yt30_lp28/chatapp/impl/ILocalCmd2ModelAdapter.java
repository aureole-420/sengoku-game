package yt30_lp28.chatapp.impl;

import common.ICRCmd2ModelAdapter;
import common.IReceiver;

/** 
 * ILocalCmd2ModelAdapter defines more methods than ICmd2ModelAdapter, 
 * enables local command to have more access to the local system
 */
public interface ILocalCmd2ModelAdapter extends ICRCmd2ModelAdapter {

	/**
	 * Add a receiver in the local view'r receiver list
	 * @param receiverStub the receiver  to add
	 */
	public void addReceiver(IReceiver receiverStub);

	/**
	 * Remove a receiver in the local view's receiver list
	 * @param receiverStub the receiver to remove
	 */
	public void removeReceiver(IReceiver receiverStub);
}
