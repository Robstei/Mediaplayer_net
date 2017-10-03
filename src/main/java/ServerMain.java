import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        View view = new View();

        Remote controller = new Controller();
        ((Controller) controller).link(model, view);
        new UDPServer(view).start();
        Scene scene = new Scene(view, 800, 500);
        primaryStage.setTitle("Music Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        Registry rmi = LocateRegistry.createRegistry(1099);
        Naming.rebind("//127.0.0.1:1099/controller",controller);
        System.out.println("Server started...");
    }
}
