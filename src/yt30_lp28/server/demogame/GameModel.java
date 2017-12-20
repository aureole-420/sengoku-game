package yt30_lp28.server.demogame;

import java.io.Serializable;
import common.IReceiver;

/**
 * Game MVC model
 */
public class GameModel implements Serializable {

	private static final long serialVersionUID = 9194200874671331035L;
	private IModel2ViewAdapter model2ViewAdapter;

	/**
	 * Constructor
	 * @param receiverStub
	 * @param model2ViewAdapter
	 */
	public GameModel(IReceiver receiverStub, IModel2ViewAdapter model2ViewAdapter) {
		this.model2ViewAdapter = model2ViewAdapter;
	}

	/**
	 * Guess a number
	 * @param g the guessed number
	 */
	public void guess(int g) {
		model2ViewAdapter.sendTo(GuessInt.class, new GuessInt(g));
	}

	/**
	 * Show result
	 * @param text the result
	 */
	public void showResult(String text) {
		model2ViewAdapter.showResult(text);
	}

	/**
	 * Start the model
	 */
	public void start() {

	}
}
