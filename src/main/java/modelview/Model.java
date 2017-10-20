package modelview;

public class Model {
    private SongList playlist = new SongList();
    private SongList allSongs = new SongList();


    public SongList getPlaylist() { return playlist; }

    public SongList getAllSongs() {
        return allSongs;
    }
}
