package server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelview.*;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage)  {
        Model model = new Model();
        View view = new View();

        try {
            Remote controller = new Controller();
            Registry rmi = LocateRegistry.createRegistry(1099);
            rmi.rebind("controller", controller);
            ((Controller) controller).link(model, view);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        new UDPServer(view).start();
        new TCPServer(model).start();
        Scene scene = new Scene(view, 800, 500);
        primaryStage.setTitle("Music Player");
        primaryStage.setScene(scene);
        primaryStage.show();


        System.out.println("Server started...");
    }
}
