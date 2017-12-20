package yt30_lp28.server.demogame;

import java.io.Serializable;
import java.rmi.RemoteException;

import common.ICRCmd2ModelAdapter;
import common.ICRMessageType;
import common.IReceiver;
import provided.mixedData.MixedDataKey;

/**
 * Game controller
 */
public class GameController implements Serializable {

	private static final long serialVersionUID = 5196008801402154379L;
	private GameModel model;
	private GameView view;

	/**
	 * Constructor
	 * @param receiverStub
	 * @param cmd2ModelAdpt
	 */
	public GameController(IReceiver receiverStub, ICRCmd2ModelAdapter cmd2ModelAdpt) {
		model = new GameModel(receiverStub, new IModel2ViewAdapter() {
			private static final long serialVersionUID = -4003250566174756042L;

			@Override
			public void showResult(String result) {
				view.showResult(result);
			}

			@Override
			public <T extends ICRMessageType> void sendTo(Class<T> class1, T g) {
				// TODO Auto-generated method stub
				cmd2ModelAdpt.sendTo(receiverStub, class1, g);
			}
		});

		view = new GameView(new IView2ModelAdapter() {
			private static final long serialVersionUID = 6631767552040526582L;

			@Override
			public void guess(int number) {
				model.guess(number);
			}
		});

		try {
			MixedDataKey<IServer2GameAdapter> key = new MixedDataKey<>(receiverStub.getUUID(), "IServer2GameAdapter",
					IServer2GameAdapter.class);
			cmd2ModelAdpt.put(key, new IServer2GameAdapter() {
				@Override
				public void showResult(String text) {
					model.showResult(text);
				}
			});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Start the game
	 */
	public void start() {
		model.start();
		view.start();
	}
}
