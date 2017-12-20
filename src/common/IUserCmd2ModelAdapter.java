package common;

/**
 * This adapter is for the externally-sourced commands to  interact with the local system,
 * specifically the DataPacketUserAlgoCmd.
 * Here we specify the target recipient in sendTo() method to IUser type.
 */
public interface IUserCmd2ModelAdapter extends ICmd2ModelAdapter<IUser, IUserMessageType> {

}