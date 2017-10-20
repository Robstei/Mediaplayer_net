package client;

import modelview.Model;
import modelview.SongList;

import java.io.*;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TCPClient extends Thread {

    private Model model;
    private final String password = "password";
    private String controllerName;
    private String username = "user";
    private InputStream inputStream;
    private ClientController clientController;

    public TCPClient (Model model, Remote clientControler) {
        this.model = model;
        this.clientController = (ClientController) clientControler;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost",5020);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);


            objectOutputStream.writeUTF(username);
            objectOutputStream.writeUTF(password);
            objectOutputStream.flush();
            boolean connection = objectInputStream.readBoolean();

            if(connection){
                try {
                    //ClientRemoteImpl clientRemote = new ClientRemoteImpl(socket);
                    Registry registry = LocateRegistry.getRegistry(1099);
                    registry.rebind("user", clientController);
                    controllerName = objectInputStream.readUTF();
                    SongList songslibary = (SongList) objectInputStream.readObject();
                    SongList playlist = (SongList) objectInputStream.readObject();
                    model.getAllSongs().addAll(songslibary);
                    model.getPlaylist().addAll(playlist);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
