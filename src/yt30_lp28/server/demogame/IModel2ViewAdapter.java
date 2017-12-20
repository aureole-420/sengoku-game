package yt30_lp28.server.demogame;

import java.io.Serializable;

import common.ICRMessageType;

/**
 * Game MVC model2view adapter
 */
public interface IModel2ViewAdapter extends Serializable {

	/**
	 * @param result
	 */
	public void showResult(String result);

	/**
	 * @param class1
	 * @param g
	 */
	public <T extends ICRMessageType> void sendTo(Class<T> class1, T g);

}
