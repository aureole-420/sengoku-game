package yt30_lp28.server.demogame;

import java.io.Serializable;

/**
 * Game mvc view2model adapter
 */
public interface IView2ModelAdapter extends Serializable {

	/**
	 * @param number
	 */
	public void guess(int number);

}
