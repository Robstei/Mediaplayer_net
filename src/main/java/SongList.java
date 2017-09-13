import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import interfaces.Song;
import javafx.collections.ModifiableObservableListBase;

public class SongList extends ModifiableObservableListBase<Song> implements interfaces.SongList, Serializable {

    private static final long serialVersionUID = 726389091202934571L;


    private ArrayList<Song> songList = new ArrayList<>();


    public boolean addSong(interfaces.Song s) throws RemoteException {
        songList.add(s);
        return true;
    }

    @Override
    public boolean deleteSong(interfaces.Song s) throws RemoteException {
        songList.remove(s.getId());
        return true;
    }

    @Override
    public void setList(ArrayList<interfaces.Song> s) throws RemoteException {
        this.songList = s;
    }

    @Override
    public ArrayList<interfaces.Song> getList() throws RemoteException {
        return songList;
    }

    @Override
    public void deleteAllSongs() throws RemoteException {
        songList = new ArrayList<>();
    }

    @Override
    public int sizeOfList() throws RemoteException {
        return getList().size();
    }

    @Override
    public Song findSongByPath(String name) throws RemoteException {
        for(int i = 0; i < sizeOfList(); i++){
            if(getList().get(i).getPath().equals(name)){
                return getList().get(i);
            }
        }
        return null;
    }

    @Override
    public Song get(int index) {
        return songList.get(index);
    }

    @Override
    public int size() {
        try {
            return getList().size();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected Song doRemove(int index) {
        try {
            getList().remove(index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            return getList().get(index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Song doSet(int index, Song element) {
        try {
            getList().set(index, element);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            return getList().get(index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void doAdd(int index, Song element) {
        try {
            getList().add(index, element);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Song findSongbyID(long id) {
        for(Song song: songList) {
            if(song.getId() == id) {
                return song;
            }
        }
        return null;
    }
}
