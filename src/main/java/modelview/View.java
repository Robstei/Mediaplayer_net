package modelview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class View extends BorderPane {

    // declaring the right side
    public Label titell;
    public TextField titeltf;
    public Label interpretl;
    public TextField interprettf;
    public Label albuml;
    public TextField albumtf;
    public Button play;
    public Button pause;
    public Button next;
    public Button commit;
    public HBox right_middle;
    public VBox right;
    public Button add_playlist;

    // declaring the left side
    public VBox left;
    public ListView<interfaces.Song> songslv;

    // declaring the center
    public VBox center;
    public ListView<interfaces.Song> playlistlv;

    // declaring the top
    public HBox top;
    public Button loadb;
    public Button saveb;
    public Label timel;
    public ComboBox cb;

    // declaring the buttom
    public Button addAllb;
    public HBox bottom;

    public View() {

        titell = new Label("Titel");
        titeltf = new TextField("");
        interpretl = new Label("Interpret");
        interprettf = new TextField("");
        albuml = new Label("Album");
        albumtf = new TextField("");
        play = new Button("\u25B6");
        pause = new Button("Pause");
        next = new Button("Next");
        commit = new Button("Commit");
        right_middle = new HBox();
        right_middle.setSpacing(3);
        right_middle.getChildren().addAll(play, pause, next, commit);
        right = new VBox();
        right.setSpacing(3);
        add_playlist = new Button("Ad To Playlist");

        right.setPadding(new Insets(3));
        right.getChildren().addAll(titell, titeltf, interpretl, interprettf, albuml, albumtf, right_middle, add_playlist);

        playlistlv = new ListView<>();

        center = new VBox();
        center.setPadding(new Insets(3));
        center.getChildren().addAll(playlistlv);
        VBox.setVgrow(playlistlv, Priority.ALWAYS);


        addAllb = new Button("Add all");
        bottom = new HBox();
        bottom.setAlignment(Pos.TOP_LEFT);
        bottom.setPadding(new Insets(3));
        bottom.getChildren().addAll(addAllb);

        loadb = new Button("Load");
        saveb = new Button("Save");
        timel = new Label("00:00");
        cb = new ComboBox();
        cb.setPrefWidth(200);
        ObservableList<String> serializingWays = FXCollections.observableArrayList(
                "Binär", "File", "XML", "SQL", "OpenJPA", "OpenJPAwithout");
        cb.setItems(serializingWays);
        cb.getSelectionModel().selectFirst();
        top = new HBox();
        top.setSpacing(10);
        top.setPadding(new Insets(3));
        top.getChildren().addAll(cb, loadb, saveb, timel);

        songslv = new ListView<>();

        left = new VBox();
        left.setPadding(new Insets(3));
        left.getChildren().addAll(songslv);
        VBox.setVgrow(songslv, Priority.ALWAYS);

        setBottom(bottom);
        setTop(top);
        setCenter(center);
        setRight(right);
        setLeft(left);
    }
}

