package server;

import interfaces.ControllerInterface;
import modelview.Model;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;

public class TCPServer extends Thread {

    private Model model;
    private final String password = "password";
    private ArrayList<ControllerInterface> clients = new ArrayList<>();

    public TCPServer(Model model) {
        this.model = model;
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(5020);

            while (true) {
                Socket socket = server.accept();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                String username = objectInputStream.readUTF();
                String passwordToCheck = objectInputStream.readUTF();

                if (password.equals(passwordToCheck)) {
                    objectOutputStream.writeBoolean(true);
                    objectOutputStream.writeUTF("controller");
                    objectOutputStream.flush();
                    Thread.sleep(1000);
                    clients.add((ControllerInterface) Naming.lookup(username));
                    new TCPClientThread(objectOutputStream, objectInputStream, model).start();
                } else {
                    objectOutputStream.writeBoolean(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
