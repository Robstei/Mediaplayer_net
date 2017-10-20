package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerInterface extends Remote {
    public void play() throws RemoteException;
    public void pause() throws RemoteException;
    public void playNextSong() throws RemoteException;
    public  void addToPlaylist() throws RemoteException;
}
