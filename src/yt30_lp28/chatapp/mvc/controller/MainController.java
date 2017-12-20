package yt30_lp28.chatapp.mvc.controller;

import java.awt.Component;
import javax.swing.SwingUtilities;

import common.IChatRoom;
import yt30_lp28.chatapp.impl.ProxyUser;
import yt30_lp28.chatapp.impl.Receiver;
import yt30_lp28.chatapp.impl.User;
import yt30_lp28.chatapp.mvc.model.*;
import yt30_lp28.chatapp.mvc.view.*;

/**
 * main-mvc controller
 */
public class MainController {

	private MainModel mainModel;
	private MainView<ProxyUser> mainView;

	/**
	 * Constructor for MainController, initialize model and view
	 */
	public MainController() {

		mainModel = new MainModel(new MainModel2ViewAdapter() {
			@Override
			public MiniMVCAdapter<ProxyUser> makeMiniMVC(IChatRoom chatRoom, User user, Receiver receiver) {
				MiniController miniController = new MiniController(chatRoom, user, new Mini2MainAdapter() {
					@Override
					public void remove(Component miniView) {
						mainView.removeTab(miniView);
					}
				}, receiver);

				MiniView<ProxyUser> miniView = miniController.getView();
				mainView.addTab(chatRoom.getName(), miniView);

				return new MiniMVCAdapter<ProxyUser>() {

					@Override
					public void addItem(ProxyUser t) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								miniView.addItem(t);
							}
						});
					}

					@Override
					public void removeItem(ProxyUser t) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								miniView.removeItem(t);
							}
						});
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
				mainView.append(text);
			}

			@Override
			public void setServerName(String localAddr) {
				mainView.setServerName(localAddr);
			}

			@Override
			public void addConnected(ProxyUser userStub) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						mainView.addConnected(userStub);
					}
				});
			}

			@Override
			public void appendUserMsg(String text, String label) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						mainView.appendUserMsg(text, label);
					}
				});
			}

			@Override
			public void addScrollComp(Component comp, String label) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						mainView.addScroll(comp, label);
					}
				});
			}

			@Override
			public void addNonScrollComp(Component comp, String label) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						mainView.addNonScroll(comp, label);
					}
				});
			}

			@Override
			public void removeConnected(ProxyUser userStub) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						mainView.removeConnected(userStub);
					}
				});
			}
		});

		mainView = new MainView<ProxyUser>(new MainView2ModelAdapter<ProxyUser>() {
			@Override
			public void createChatRoom(String name) {
				(new Thread() {
					@Override
					public void run() {
						mainModel.createChatRoom(name);
					}
				}).start();
			}

			@Override
			public void connectTo(String remoteHost) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						mainModel.connectToPeer(remoteHost);
					}

				}).start();
			}

			@Override
			public void stop() {
				mainModel.stop();
			}

			@Override
			public void request(ProxyUser user) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						mainModel.request(user);
					}
				}).start();
			}

			@Override
			public void startServer(String name, String addr) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						mainModel.startServer(name, addr);
					}
				}).start();
			}

			@Override
			public void sendMsg(String text, ProxyUser target) {
				(new Thread() {
					@Override
					public void run() {
						mainModel.sendMsg(text, target);
					}
				}).start();
			}

			@Override
			public void sendComp(ProxyUser target) {
				// TODO Auto-generated method stub
				(new Thread() {
					@Override
					public void run() {
						mainModel.sendComp(target);
					}
				}).start();
			}
		});
	}

	/**
	 * Start the application
	 */
	public void start() {
		mainModel.start();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					mainView.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Launch the application.
	 * @param args comand line arguments
	 */
	public static void main(String[] args) {
		new MainController().start();
	}
}
