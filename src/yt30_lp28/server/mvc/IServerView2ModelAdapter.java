package yt30_lp28.server.mvc;

/**
 * view2model adapter
 */
public interface IServerView2ModelAdapter {
	/**
	 * Start the game
	 */
	public void startGame();

	/**
	 * Send choose team gui
	 */
	public void sendTeams();

	/**
	 * Send SengokuGame 
	 */
	public void sendSengokuGame();
}
