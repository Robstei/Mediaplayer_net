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

import java.io.*;
import java.rmi.RemoteException;
import java.util.List;


public class Controller implements EventHandler<ActionEvent> {

    Song s;
    private Model model;
    private View view;
    private String time;
    private Media music;
    private MediaPlayer mp;
    private FileChooser fileChooser;
    private BinaryStrategy bs;
    private XMLStrategy xml;
    int count;


    //Verbidung zwischen den Daten im Model und der Liste in der View.
    //Die Liste wird automatisch über Änderungen informiert und aktualisiert sich.
    //Seit Java 1.8 können Lambda-Ausdrücke verwendet werden.
    //Da EventHandler ein funktionales Interface ist (besitzt nur eine Methodensignatur),
    //kann hier elegant mit Lambda-Ausdrüken gearbeitet werden.

    public void link(Model model, View view) throws RemoteException {

        model.getAllSongs().getList();

        bs = new BinaryStrategy();
        xml = new XMLStrategy();
        s = new Song("AA","B","C","D");

        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "MP3 Files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extensionFilter);
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
        view.addAllb.setOnAction(this);
        view.next.setOnAction(this);
        view.saveb.setOnAction(this);

        view.playlistlv.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                ((Song) oldValue).albumProperty().unbindBidirectional(view.albumtf.textProperty());
                ((Song) oldValue).interpretProperty().unbindBidirectional(view.interprettf.textProperty());
            }
            view.albumtf.setText(((Song) newValue).albumProperty().get());
            ((Song) newValue).albumProperty().bindBidirectional(view.albumtf.textProperty());

            view.interprettf.setText(((Song) newValue).interpretProperty().get());
            ((Song) newValue).interpretProperty().bindBidirectional(view.interprettf.textProperty());

            view.titeltf.setText((newValue).getTitle());
        });

        Callback<ListView<interfaces.Song>, ListCell<interfaces.Song>> c = new Callback<ListView<interfaces.Song>, ListCell<interfaces.Song>>() {
            @Override
            public ListCell<interfaces.Song> call(ListView<interfaces.Song> param) {
                ListCell<interfaces.Song> cell = new ListCell<interfaces.Song>() {
                    public void updateItem(interfaces.Song item, boolean empty) {
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


        view.songslv.setItems(model.getAllSongs());
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
        view.playlistlv.setItems(model.getPlaylist());
        view.playlistlv.setCellFactory(plcallback);
    }

    private void makeMediaPlayer(String path) {
        music = new Media(path);
        mp = new MediaPlayer(music);
        mp.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            time = String.format("%02d:%02d", (long) newValue.toMinutes(), ((long) newValue.toSeconds()) % 60);
            view.timel.setText(time);
        });
        mp.setOnEndOfMedia(() -> {
            nextSong();
        });
        mp.setVolume(0.05);
        mp.play();
    }

    private void nextSong() {
        try {
            for (int i = 0; i < model.getPlaylist().sizeOfList() - 1; i++) {
                if (model.getPlaylist().get(i).getPath().equals(music.getSource())) {
                    mp.stop();
                    makeMediaPlayer(view.playlistlv.getItems().get(i + 1).getPath());
                    break;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent event)  {
        if (event.getSource() == view.add_playlist && model.getAllSongs().size() > 0) {
            if (view.songslv.getSelectionModel().getSelectedItem() != null) {
                model.getPlaylist().add(view.songslv.getSelectionModel().getSelectedItem());
            } else if (view.songslv.getItems().get(0) != null) {
                model.getPlaylist().add(model.getAllSongs().get(0));
            }
        } else if (event.getSource() == view.play) {
            if (view.playlistlv.getItems().get(0) != null) {
                if (mp == null) {
                    makeMediaPlayer(view.playlistlv.getItems().get(0).getPath());
                } else if (mp.getStatus() == MediaPlayer.Status.PAUSED) {
                    mp.play();
                }
            }
        } else if (event.getSource() == view.pause) {
            mp.pause();
        } else if (event.getSource() == view.loadb) {
            try{
                Song song;
                switch (view.cb.getSelectionModel().getSelectedItem().toString()) {
                    case "File":
                        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
                        if (list != null) {
                            model.getAllSongs().deleteAllSongs();
                            for (File file : list) {
                                song = new Song(file.toURI().toString(), file.getName(), "", "");
                                model.getAllSongs().add(song);
                            }
                        }
                        break;
                    case "Binär":
                        bs.openReadableSongs();
                        try {
                            while(true) {
                            song = bs.readSong();
                            model.getAllSongs().add(song);
                            }
                        }
                        catch (EOFException e) {
                            bs.closeReadable();
                        }
                        bs.openReadablePlaylist();
                        try {
                            while(true) {
                                song = bs.readSong();
                                model.getPlaylist().add(song);
                            }
                        }
                        catch (EOFException e) {
                            bs.closeReadable();
                        }
                        break;
                    case "XML":
                        xml.openReadableSongs();
                        count = xml.readCount();
                        for (int i = 0; i<count;i++) {
                            song = xml.readSong();
                            model.getAllSongs().add(song);
                        }
                        xml.closeReadable();

                        xml.openReadablePlaylist();
                        count = xml.readCount();
                        for (int i = 0; i<count;i++) {
                            song = xml.readSong();
                            model.getPlaylist().add(song);
                        }
                        xml.closeReadable();
                        break;
                    case "C":
                        break;
                    case "D":
                        break;
                }
            } catch (IOException e) {
                System.out.println("IOException swtich");
                e.printStackTrace(System.out);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == view.addAllb) {
            model.getPlaylist().addAll(model.getAllSongs());
        } else if (event.getSource() == view.next) {
            nextSong();
        } else if (event.getSource() == view.saveb) {
            try{
                switch (view.cb.getSelectionModel().getSelectedItem().toString()) {
                    case "Binär":
                        bs.openWriteableSongs();
                        for(int i = 0; i < model.getAllSongs().sizeOfList(); i++) {
                            bs.writeSong(model.getAllSongs().get(i));
                        }
                        bs.closeWriteable();
                        bs.openWriteablePlaylist();
                        for(int i = 0; i < model.getPlaylist().sizeOfList(); i++) {
                            bs.writeSong(model.getPlaylist().get(i));
                        }
                        bs.closeWriteable();
                        break;

                    case "XML":
                        xml.openWriteableSongs();
                        xml.writeCount(model.getAllSongs().sizeOfList());
                        for(int i = 0; i < model.getAllSongs().sizeOfList(); i++) {
                            xml.writeSong(model.getAllSongs().get(i));
                        }
                        xml.closeWriteable();
                        xml.openWriteablePlaylist();
                        xml.writeCount(model.getPlaylist().sizeOfList());
                        for(int i = 0; i < model.getPlaylist().sizeOfList(); i++) {
                            xml.writeSong(model.getPlaylist().get(i));
                        }
                        xml.closeWriteable();
                        break;

                    case"C":
                        break;
                    case"D":
                        break;
                }
            } catch (IOException e) {

            }
        }
    }
}

