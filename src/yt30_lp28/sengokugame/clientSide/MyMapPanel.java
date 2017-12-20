package yt30_lp28.sengokugame.clientSide;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import map.IRightClickAction;
import map.MapPanel;

/**
 * Inherited map panel featured with both right and left click actions 
 */
public class MyMapPanel extends MapPanel {

	private static final long serialVersionUID = -7825637209172288638L;

	/**
	 * {@inheritDoc}
	 */
	public MyMapPanel() {
	}

	/**
	 * {@inheritDoc} 
	 * @param globeTypeClass
	 */
	public MyMapPanel(Class<? extends Globe> globeTypeClass) {
		super(globeTypeClass);
	}

	/**
	 * Define and add the left click action
	 * @param leftClick the left click action.
	 */
	public void addLeftClickAction(final ILeftClickAction leftClick) {
		MouseListener listener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					Position p = getWWD().getCurrentPosition();
					leftClick.apply(p);
				}
			}
		};
		getWWD().addMouseListener(listener);
	}
}
