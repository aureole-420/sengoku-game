package yt30_lp28.chatapp.mvc.controller;

import java.awt.Component;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;

import common.IChatRoom;
import common.IReceiver;
import yt30_lp28.chatapp.impl.ProxyUser;
import yt30_lp28.chatapp.impl.Receiver;
import yt30_lp28.chatapp.impl.User;
import yt30_lp28.chatapp.mvc.model.MiniModel;
import yt30_lp28.chatapp.mvc.view.MiniView;
import yt30_lp28.chatapp.mvc.view.MiniView2ModelAdapter;

/**
 * mini mvc controller
 */
public class MiniController {

	private MiniModel model;
	private MiniView<ProxyUser> view;

	/**
	 * Constructor for MiniController, initialize mini model and mini view
	 * @param chatRoom the chat room object corresponding to this mini mvc
	 * @param user the local user
	 * @param mini2MainAdpt adapter talking to main mvc
	 * @param receiver the local receiver corresponding to this mini mvc
	 */
	public MiniController(IChatRoom chatRoom, User user, Mini2MainAdapter mini2MainAdpt, Receiver receiver) {

		model = new MiniModel(chatRoom, user, receiver);

		view = new MiniView<ProxyUser>(new MiniView2ModelAdapter() {
			@Override
			public void leave(Component comp) {
				mini2MainAdpt.remove(comp);
				model.leave();
			}

			@Override
			public void sendText(String msg) {
				model.sendMsg(msg);
			}

			@Override
			public void sendImage(ImageIcon imageIcon) {
				model.sendImage(imageIcon);

			}

			@Override
			public void sendComp() {
				model.sendComp();
			}
		});

		for (IReceiver u : chatRoom.getIReceiverStubs()) {
			try {
				view.addItem(new ProxyUser(u.getUserStub()));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		view.append("Successful create chat room " + chatRoom.getName() + " by " + user);
	}

	/**
	 * Get mini view
	 * @return mini view
	 */
	public MiniView<ProxyUser> getView() {
		return view;
	}

	/** Get mini model 
	 * @return mini model
	 */
	public MiniModel getModel() {
		return model;
	}
}
