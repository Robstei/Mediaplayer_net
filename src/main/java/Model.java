import javafx.stage.Stage;


public class Model {
    private Stage primaryStage = null;
    SongList playlist = new SongList();
    SongList allSongs = new SongList();

    public SongList getPlaylist() {
        return playlist;
    }

    public SongList getAllSongs() {
        return allSongs;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
