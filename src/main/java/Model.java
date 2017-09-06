import javafx.stage.Stage;


public class Model {
    private Stage primaryStage = new Stage();
    private SongList playlist = new SongList();
    private SongList allSongs = new SongList();

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
