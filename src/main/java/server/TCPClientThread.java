package server;

import modelview.Model;
import modelview.SongList;

import java.io.*;
import java.net.Socket;

public class TCPClientThread extends Thread {

    private Model model;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public TCPClientThread(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Model model) {
        this.model = model;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        try {
            SongList songslibary = model.getAllSongs();
            SongList playlist = model.getPlaylist();
            objectOutputStream.writeObject(songslibary);
            objectOutputStream.writeObject(playlist);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
