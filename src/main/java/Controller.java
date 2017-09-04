import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.media.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;


public class Controller implements EventHandler<ActionEvent> {

    private Model model;
    private View view;
    Media music;
    MediaPlayer mp;
    FileChooser fileChooser;
    int i;
    ObservableList<interfaces.Song> allSongs;
    ObservableList<interfaces.Song> playlist;


        //Verbidung zwischen den Daten im Model und der Liste in der View.
        //Die Liste wird automatisch über Änderungen informiert und aktualisiert sich.
        //Seit Java 1.8 können Lambda-Ausdrücke verwendet werden.
        //Da EventHandler ein funktionales Interface ist (besitzt nur eine Methodensignatur),
        //kann hier elegant mit Lambda-Ausdrüken gearbeitet werden.

        public void link(Model model, View view) throws RemoteException {

            allSongs =FXCollections.observableArrayList();
            playlist =FXCollections.observableArrayList();
            this.model = model;
            this.view = view;
            view.add_playlist.setOnAction(this);

            String path = new File("C:/Users/Robin/Downloads/Sound.mp3").getAbsolutePath();
            System.out.println(path);
            String uri = new File("C:/Users/Robin/Downloads/Sound.mp3").toURI().toString();
            System.out.println(uri);

            view.play.setOnAction(this);
            view.pause.setOnAction(this);
            view.loadb.setOnAction(this);

            Song aq = (Song) view.playlistlv.getSelectionModel().getSelectedItem();
            if (aq != null) {
                aq.albumProperty().bindBidirectional(view.albumtf.textProperty());
            }

            view.playlistlv.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println(observable);
                System.out.println(oldValue);
                System.out.println(newValue);
                if (oldValue != null) {
                    ((Song) oldValue).albumProperty().unbindBidirectional(view.albumtf.textProperty());
                }
                view.albumtf.setText(((Song) newValue).albumProperty().get());
                ((Song) newValue).albumProperty().bindBidirectional(view.albumtf.textProperty());
            });

            fileChooser = new FileChooser();

            view.songslv.setItems(allSongs);

            Callback<ListView<interfaces.Song>, ListCell<interfaces.Song>> c = new Callback <ListView<interfaces.Song>, ListCell<interfaces.Song>>() {
                @Override
                public ListCell<interfaces.Song> call(ListView<interfaces.Song> param) {
                    ListCell<interfaces.Song> cell = new ListCell<interfaces.Song>() {
                        public void updateItem(interfaces.Song item, boolean empty){
                            super.updateItem(item, empty);
                            if (item != null) {
                                this.setText(item.getTitle());
                            } else {
                                setText("");
                            }
                        }
                    };
                    return cell;
                }
            };
            view.songslv.setCellFactory(c);


            Callback<ListView<interfaces.Song>, ListCell<interfaces.Song>> plcallback = new Callback<ListView<interfaces.Song>, ListCell<interfaces.Song>>() {
                @Override
                public ListCell<interfaces.Song> call(ListView<interfaces.Song> param) {
                    ListCell<interfaces.Song> lc = new ListCell<interfaces.Song>() {
                        public void updateItem(interfaces.Song item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                setText(item.getTitle());
                            } else {
                                setText("");
                            }
                        }
                    };
                    return lc;
                }
            };
            view.playlistlv.setItems(playlist);
            view.playlistlv.setCellFactory(plcallback);
        }

    @Override
    public void handle(ActionEvent event) {
            if(event.getSource() == view.add_playlist && allSongs.size() > 0){
                if(view.songslv.getSelectionModel().getSelectedItem() != null) {
                    playlist.add(view.songslv.getSelectionModel().getSelectedItem());
                } else if(view.songslv.getItems().get(0) != null){
                    playlist.add(allSongs.get(0));
                }
            } else  if(event.getSource() == view.play){
                System.out.println(view.playlistlv.getSelectionModel().getSelectedItem());
                if(mp == null) {
                    music = new Media(view.playlistlv.getItems().get(0).getPath());
                    mp = new MediaPlayer(music);
                    mp.setVolume(0.05);
                }
                mp.play();
            } else  if(event.getSource() == view.pause){
                mp.pause();
            } else  if(event.getSource() == view.loadb) {
                List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
                if(list != null) {
                    for (File file : list) {
                        Song song = new Song(file.toURI().toString(),file.getName(),"","");
                        try {
                            model.allSongs.addSong(song);
                            allSongs.add(song);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
}
