

import java.rmi.RemoteException;
import java.util.ArrayList;

import javafx.collections.ModifiableObservableListBase;

public class SongList extends ModifiableObservableListBase<interfaces.Song> implements interfaces.SongList {

    private ArrayList<interfaces.Song> songList = new ArrayList<interfaces.Song>();

    @Override
    public boolean addSong(interfaces.Song s) throws RemoteException {
        songList.add(s);
        return true;
    }

    @Override
    public boolean deleteSong(interfaces.Song s) throws RemoteException {
        return false;
    }

    @Override
    public void setList(ArrayList<interfaces.Song> s) throws RemoteException {

    }

    @Override
    public ArrayList<interfaces.Song> getList() throws RemoteException {
        return songList;
    }

    @Override
    public void deleteAllSongs() throws RemoteException {

    }

    @Override
    public int sizeOfList() throws RemoteException {
        return 0;
    }

    @Override
    public Song findSongByPath(String name) throws RemoteException {
        return null;
    }

    @Override
    public Song get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    protected Song doRemove(int index) {
        return null;
    }

    @Override
    protected Song doSet(int index, interfaces.Song element) {
        return null;
    }

    @Override
    protected void doAdd(int index, interfaces.Song element) {

    }
}
