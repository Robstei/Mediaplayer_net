import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Controller extends UnicastRemoteObject implements EventHandler<ActionEvent>, ControllerInterface {

    private int count;
    private int count2;
    private Song s;
    private Model model;
    private View view;
    private String time;
    private Media music;
    private MediaPlayer mp;
    private FileChooser fileChooser;
    private BinaryStrategy bs;
    private XMLStrategy xml;
    private JDBCStrategy jdb;
    private OpenJPAStrategy ojpa;

    protected Controller() throws RemoteException {
    }


    //Verbidung zwischen den Daten im Model und der Liste in der View.
    //Die Liste wird automatisch über Änderungen informiert und aktualisiert sich.
    //Seit Java 1.8 können Lambda-Ausdrücke verwendet werden.
    //Da EventHandler ein funktionales Interface ist (besitzt nur eine Methodensignatur),
    //kann hier elegant mit Lambda-Ausdrüken gearbeitet werden.

    public void link(Model model, View view) {

        model.getAllSongs().getList();

        bs = new BinaryStrategy();
        xml = new XMLStrategy();
        jdb = new JDBCStrategy();
        ojpa = new OpenJPAStrategy();

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
        for (int i = 0; i < model.getPlaylist().sizeOfList() - 1; i++) {
            if (model.getPlaylist().get(i).getPath().equals(music.getSource())) {
                mp.stop();
                makeMediaPlayer(view.playlistlv.getItems().get(i + 1).getPath());
                break;
            }
        }
    }


    @Override
    public void play() {
        if (view.playlistlv.getItems().get(0) != null) {
            if (mp == null) {
                makeMediaPlayer(view.playlistlv.getItems().get(0).getPath());
            } else if (mp.getStatus() == MediaPlayer.Status.PAUSED) {
                mp.play();
            }
        }
    }

    @Override
    public void pause() {
        mp.pause();
    }

    @Override
    public void PlayNextSong() {

    }


    @Override
    public void handle(ActionEvent event) {

        // Add Playlist
        if (event.getSource() == view.add_playlist && model.getAllSongs().size() > 0) {
            if (view.songslv.getSelectionModel().getSelectedItem() != null) {
                model.getPlaylist().add(view.songslv.getSelectionModel().getSelectedItem());
            } else if (view.songslv.getItems().get(0) != null) {
                model.getPlaylist().add(model.getAllSongs().get(0));
            }

            //Play
        } else if (event.getSource() == view.play) {
           play();

            //Pause
        } else if (event.getSource() == view.pause) {
            pause();
            //Add All
        } else if (event.getSource() == view.addAllb) {
            model.getPlaylist().addAll(model.getAllSongs());

            //Next
        } else if (event.getSource() == view.next) {
            nextSong();

            //Load
        } else if (event.getSource() == view.loadb) {
            try {
                Song song;
                switch (view.cb.getSelectionModel().getSelectedItem().toString()) {
                    case "File":
                        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
                        if (list != null) {
                            model.getAllSongs().deleteAllSongs();
                            for (File file : list) {
                                song = new Song(file.toURI().toString(), file.getName(), "", "");
                                Naming.rebind("//127.0.0.1:1099/"+ song.getId(), song);
                                model.getAllSongs().add(song);
                            }
                        }
                        break;
                    case "Binär":
                        bs.openReadableSongs();
                        try {
                            while (true) {
                                song = bs.readSong();
                                model.getAllSongs().add(song);
                            }
                        } catch (EOFException e) {
                            bs.closeReadable();
                        }
                        bs.openReadablePlaylist();
                        try {
                            while (true) {
                                song = bs.readSong();
                                model.getPlaylist().add(song);
                            }
                        } catch (EOFException e) {
                            bs.closeReadable();
                        }
                        break;
                    case "XML":
                        xml.openReadableSongs();
                        count = xml.readCount();
                        for (int i = 0; i < count; i++) {
                            song = xml.readSong();
                            model.getAllSongs().add((interfaces.Song) song);
                        }
                        xml.closeReadable();

                        xml.openReadablePlaylist();
                        count = xml.readCount();
                        for (int i = 0; i < count; i++) {
                            song = xml.readSong();
                            model.getPlaylist().add((interfaces.Song) song);
                        }
                        xml.closeReadable();
                        break;
                    case "SQL":
                        count = jdb.songsCount();
                        count2 = jdb.playlistCount();
                        jdb.openReadableSongs();
                        for (int i = 0; i < count; i++) {
                            model.getAllSongs().add(jdb.readSong());
                        }
                        jdb.openReadablePlaylist();
                        for (int i = 0; i < count2; i++) {
                            model.getPlaylist().add(jdb.readSong());
                        }
                        break;
                    case "OpenJPA":
                        ojpa.openReadableSongs();
                        try {
                            while ((s = ojpa.readSong()) != null) {
                                model.getAllSongs().add(s);
                            }
                        } catch (NullPointerException n) {
                            ojpa.closeReadable();
                        }
                        //TODO Serializing Playlist via OpenJPA
                        break;
                    case "OpenJPAwithout":
                        //TODO Serialinzing libary and playlist without persistence.xml
                        break;
                }
            } catch (IOException e) {
                System.out.println("IOException swtich");
                e.printStackTrace(System.out);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //Save
        } else if (event.getSource() == view.saveb) {
            try {
                switch (view.cb.getSelectionModel().getSelectedItem().toString()) {
                    case "Binär":
                        bs.openWriteableSongs();
                        for (int i = 0; i < model.getAllSongs().sizeOfList(); i++) {
                            bs.writeSong(model.getAllSongs().get(i));
                        }
                        bs.closeWriteable();
                        bs.openWriteablePlaylist();
                        for (int i = 0; i < model.getPlaylist().sizeOfList(); i++) {
                            bs.writeSong(model.getPlaylist().get(i));
                        }
                        bs.closeWriteable();
                        break;

                    case "XML":
                        xml.openWriteableSongs();
                        xml.writeCount(model.getAllSongs().sizeOfList());
                        for (int i = 0; i < model.getAllSongs().sizeOfList(); i++) {
                            xml.writeSong(model.getAllSongs().get(i));
                        }
                        xml.closeWriteable();
                        xml.openWriteablePlaylist();
                        xml.writeCount(model.getPlaylist().sizeOfList());
                        for (int i = 0; i < model.getPlaylist().sizeOfList(); i++) {
                            xml.writeSong(model.getPlaylist().get(i));
                        }
                        xml.closeWriteable();
                        break;
                    case "SQL":
                        jdb.openWriteableSongs();

                        for (interfaces.Song s : model.getAllSongs()) {
                            jdb.writeSong(s);
                        }
                        jdb.openWriteablePlaylist();
                        for (interfaces.Song s : model.getPlaylist()) {
                            jdb.writeSong(s);
                        }
                        break;
                    case "OpenJPA":
                        ojpa.openWriteableSongs();
                        for (interfaces.Song s : model.getAllSongs()) {
                            ojpa.writeSong(s);
                        }
                        ojpa.closeWriteable();
                        break;
                    case "OpenJPAwithout":
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

