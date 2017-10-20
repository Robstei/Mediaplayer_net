package client;

import interfaces.Song;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import modelview.*;
import interfaces.ControllerInterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientController implements ControllerInterface, Serializable, EventHandler<ActionEvent> {

    private transient View view;
    private transient Model model;

    ControllerInterface mainController;

    public ClientController() throws RemoteException, NotBoundException, MalformedURLException {
    }


    public void link(View view, Model model) throws RemoteException {
        try {
            mainController = (ControllerInterface) Naming.lookup("controller");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.view = view;
        this.model = model;

        Callback<ListView<Song>, ListCell<Song>> c = new Callback<ListView<interfaces.Song>, ListCell<interfaces.Song>>() {
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

        view.play.setOnAction(this);
        view.pause.setOnAction(this);
    }

    @Override
    public void play() throws RemoteException {
        mainController.play();
    }

    @Override
    public void pause() throws RemoteException {
            mainController.pause();
    }

    @Override
    public void playNextSong() throws RemoteException {
        mainController.playNextSong();
    }

    @Override
    public void addToPlaylist() throws RemoteException {
        mainController.addToPlaylist();
    }


    public void refresh() {
        view.playlistlv.refresh();
        view.songslv.refresh();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == view.play) {
            try {
                play();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == view.pause) {
            try {
                pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == view.add_playlist) {

        } else if (event.getSource() == view.next) {
            try {
                playNextSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
