package yt30_lp28.server.mvc;

import java.awt.Component;

import javax.swing.SwingUtilities;

import common.IChatRoom;
import yt30_lp28.chatapp.impl.ProxyUser;
import yt30_lp28.chatapp.impl.User;
import yt30_lp28.chatapp.mvc.controller.Mini2MainAdapter;
import yt30_lp28.chatapp.mvc.controller.MiniController;
import yt30_lp28.chatapp.mvc.model.MiniMVCAdapter;
import yt30_lp28.chatapp.mvc.view.MiniView;
import yt30_lp28.server.impl.ServerReceiver;

/**
 * MVC controller
 */
public class ServerController {

	private ServerView<ProxyUser> serverView;
	private ServerModel serverModel;

	/**
	 * Constructor for ServerController
	 */
	public ServerController() {
		serverView = new ServerView<ProxyUser>(new IServerView2ModelAdapter() {
			@Override
			public void startGame() {
				serverModel.startGame();
			}

			@Override
			public void sendTeams() {
				serverModel.sendTeams();
			}

			@Override
			public void sendSengokuGame() {
				serverModel.sendSengokuGame();
			}
		});

		serverModel = new ServerModel(new IServerModel2ViewAdapter() {
			@Override
			public MiniMVCAdapter<ProxyUser> makeMiniMVC(IChatRoom chatRoom, User user, ServerReceiver receiver) {
				MiniController miniController = new MiniController(chatRoom, user, new Mini2MainAdapter() {
					@Override
					public void remove(Component miniView) {
						serverView.removeTab(miniView);
					}
				}, receiver);

				MiniView<ProxyUser> miniView = miniController.getView();
				serverView.addTab(chatRoom.getName(), miniView);

				return new MiniMVCAdapter<ProxyUser>() {
					@Override
					public void addItem(ProxyUser t) {
						miniView.addItem(t);
					}

					@Override
					public void removeItem(ProxyUser t) {
						miniView.removeItem(t);
					}

					@Override
					public void append(String text) {
						miniView.append(text);
					}

					@Override
					public void addScrollableComponent(Component comp, String label) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								miniView.addScrollableComponent(comp, label);
							}
						});
					}

					@Override
					public void quit() {
						miniController.getModel().leave();
					}

					@Override
					public void addNonScrollableComponent(Component comp, String label) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								miniView.addNonScrollableComponent(comp, label);
							}
						});
					}
				};
			}

			@Override
			public void append(String text) {
				serverView.append(text);
			}

			@Override
			public void setServerName(String localAddr) {
				serverView.setServerName(localAddr);
			}

			@Override
			public void addConnected(ProxyUser userStub) {
				serverView.addConnected(userStub);
			}

			@Override
			public void setUserName(String name) {
				serverView.setUserName(name);
			}

			@Override
			public void removeConnected(ProxyUser userStub) {
				// TODO Auto-generated method stub
				serverView.removeConnected(userStub);
			}
		});

	}

	/**
	 * Start the application
	 */
	public void start() {
		serverModel.start();
		serverView.start();
	}

	/**
	 * Launch the application.
	 * @param args comand line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new ServerController().start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
