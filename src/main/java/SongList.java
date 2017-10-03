import interfaces.Song;
import javafx.collections.ModifiableObservableListBase;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class SongList extends ModifiableObservableListBase<Song> implements interfaces.SongList {

    private static final long serialVersionUID = 726389091202934571L;


    private ArrayList<interfaces.Song> songList = new ArrayList<>();

    @Override
    public boolean addSong(interfaces.Song s) {
        songList.add(s);
        return true;
    }

    @Override
    public boolean deleteSong(interfaces.Song s) {
        songList.remove(s.getId());
        return true;
    }

    @Override
    public ArrayList<interfaces.Song> getList() {
        return songList;
    }

    @Override
    public void setList(ArrayList<interfaces.Song> s) {
        this.songList = s;
    }

    @Override
    public void deleteAllSongs() {
        songList = new ArrayList<>();
    }

    @Override
    public int sizeOfList() {
        return getList().size();
    }

    @Override
    public Song findSongByPath(String name) {
        for (int i = 0; i < sizeOfList(); i++) {
            if (getList().get(i).getPath().equals(name)) {
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
        return getList().size();
    }

    @Override
    protected Song doRemove(int index) {
        getList().remove(index);
        return getList().get(index);
    }

    @Override
    protected Song doSet(int index, Song element) {
        getList().set(index, element);
        return getList().get(index);
    }

    @Override
    protected void doAdd(int index, Song element) {
        getList().add(index, element);
    }

    public Song findSongbyID(long id) {
        for (Song song : songList) {
            if (song.getId() == id) {
                return song;
            }
        }
        return null;
    }
}
