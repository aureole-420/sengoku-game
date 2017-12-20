package yt30_lp28.sengokugame.clientSide;

import gov.nasa.worldwind.geom.Position;

/**
 * The left click action
 * @author lp28, yt30
 *
 */
public interface ILeftClickAction {
	/**
	 * The action being carried out when left clicking
	 * @param p the position of left click
	 */
	public void apply(Position p);

}
