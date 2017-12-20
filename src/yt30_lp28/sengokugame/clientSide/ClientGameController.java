package yt30_lp28.sengokugame.clientSide;

import javax.swing.DefaultComboBoxModel;

import common.ICRCmd2ModelAdapter;
import common.ICRMessageType;
import common.IReceiver;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import map.IRightClickAction;
import yt30_lp28.sengokugame.clientSide.IClientGameModel2ViewAdapter;
import yt30_lp28.sengokugame.serverSide.CityIcon;

/**
 * The Controller of game MVC on client side.
 * @author lp28, yt30
 *
 */
public class ClientGameController implements ICRMessageType {

	private static final long serialVersionUID = 6050140849824393178L;
	/**
	 * The game model
	 */
	private ClientGameModel _gameModel;
	/**
	 * The game view;
	 */
	private ClientGameView<String> _gameView;
	/**
	 * The server's receiver stub;
	 */
	private IReceiver serverStub;

	/**
	 * Constructor of the client game controller
	 * @param serverStub0 server's stub
	 * @param cmd2ModelAdpt cmd 2 model adapter;
	 */
	public ClientGameController(IReceiver serverStub0, ICRCmd2ModelAdapter cmd2ModelAdpt) {
		this.serverStub = serverStub0;

		_gameModel = new ClientGameModel(serverStub, new IClientGameModel2ViewAdapter() {
			@Override
			public void addLayer(Layer layer) {
				_gameView.addLayer(layer);
			}

			@Override
			public void removeLayer(Layer layer) {
				_gameView.removeLayer(layer);
			}

			@Override
			public <T extends ICRMessageType> void sendTo(Class<T> id, T data) {
				cmd2ModelAdpt.sendTo(serverStub, id, data);
			}

			@Override
			public DefaultComboBoxModel<String> getCBModelCityTo() {
				// TODO Auto-generated method stub
				return _gameView.getCBModelCityTo();
			}

			@Override
			public DefaultComboBoxModel<String> getCBModelCityFrom() {
				// TODO Auto-generated method stub
				return _gameView.getCBModelCityFrom();
			}

			@Override
			public void updateBaseSoldierNumber(int num) {
				_gameView.updateBaseSoldierNumber(num);
			}

			@Override
			public String selectedFromCity() {
				return _gameView.selectedFromCity();
			}
		});

		_gameView = new ClientGameView<String>(new IClientGameView2ModelAdapter<String>() {

			@Override
			public void sendDecision(Object cityFrom, Object cityTo, int noCtz2Move) {
				_gameModel.sendDecision(cityFrom, cityTo, noCtz2Move);
			}

			@Override
			public <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data) {
				cmd2ModelAdpt.sendToChatRoom(id, data);
			}

			@Override
			public <T extends ICRMessageType> void sendInterTeamMsg(Class<T> id, T data) {
				cmd2ModelAdpt.sendTo(serverStub, id, data);
			}

			@Override
			public int getNumSoldiersInBase(String city) {
				return _gameModel.getNumSoldiersInBase(city);
			}

			@Override
			public void updateRangeLayer(String baseCity) {
				_gameModel.updateRangeLayer(baseCity);

			}
		}, new IRightClickAction() {

			@Override
			public void apply(Position p) {
				_gameModel.setRightClickTargetCity(p);
			}
		}, new ILeftClickAction() {

			@Override
			public void apply(Position p) {
				_gameModel.setLeftClickBaseCity(p);
			}
		});
	}

	/**
	 * Start the controller;
	 */
	public void start() {
		System.out.println("Game Start----------");
		_gameModel.start();
		_gameView.start();
	}

	/**
	 * Getter for the client game model
	 * @return the client game model;
	 */
	public ClientGameModel getModel() {
		return _gameModel;
	}

	/**
	 * getter for the client view 
	 * @return the client view;
	 */
	public ClientGameView<String> getView() {
		return _gameView;
	}
}
