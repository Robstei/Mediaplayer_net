package client;

import interfaces.ClientRemoteInterface;
import modelview.SongList;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

public class ClientRemoteImpl implements ClientRemoteInterface, Serializable {

    transient InputStream inputStream;
    transient ObjectInputStream objectInputStream;
    transient Socket socket;

    public ClientRemoteImpl(Socket socket){
        this.socket = socket;
        try {
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public SongList requestSongs() {
        try {
            SongList songslibary = (SongList) objectInputStream.readObject();
            return songslibary;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SongList requestPlaylist(){
        try {
            SongList playlist = (SongList) objectInputStream.readObject();
            return playlist;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
