package client;

import modelview.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Model model = new Model();
        View view = new View();

        Remote clientController = null;
        try {
            clientController = new ClientController();
            ((ClientController)clientController).link(view, model);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new UDPClient(view).start();
        if(clientController != null) {
            new TCPClient(model, clientController).start();
        } else {
            System.err.println("Client konnte nicht erstellt werden");
        }

        Scene scene = new Scene(view, 800, 500);
        primaryStage.setTitle("Music Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
