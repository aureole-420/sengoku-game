package yt30_lp28.sengokugame.serverSide;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import yt30_lp28.server.impl.IServerCRCmd2ModelAdapter;

/**
 *  The cmd for round decision msgtype
 */
public class RoundDecisionCmd extends DataPacketCRAlgoCmd<RoundDecisionMsg> {
	private static final long serialVersionUID = 2999888097791037275L;
	/**
	 * Cmd to model adapter 
	 */
	private transient ICRCmd2ModelAdapter _cmd2Model;

	/**
	 * Constructor for the round decision cmd
	 * @param cmd2Model0 the cmd 2 model adapter;
	 */
	public RoundDecisionCmd(ICRCmd2ModelAdapter cmd2Model0) {
		this._cmd2Model = cmd2Model0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(Class<?> index, DataPacketCR<RoundDecisionMsg> host, String... params) {
		((IServerCRCmd2ModelAdapter) _cmd2Model).getSengokuGameModel().getDecisionMsgQueue().offer(host);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {
	}

}
