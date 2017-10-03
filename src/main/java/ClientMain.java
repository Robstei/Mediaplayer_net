import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class ClientMain extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        View view = new View();

        ClientController clientController = new ClientController();
        clientController.link(view, model);
        new UDPClient(view).start();
        Scene scene = new Scene(view, 800, 500);
        primaryStage.setTitle("Music Player");
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
