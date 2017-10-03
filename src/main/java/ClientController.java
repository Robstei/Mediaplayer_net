import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientController implements ControllerInterface, EventHandler<ActionEvent> {

    private View view;
    private Model model;
    ControllerInterface clientController = (ControllerInterface) Naming.lookup("controller");
    Method method;

    public ClientController() throws RemoteException, NotBoundException, MalformedURLException {
    }


    public void link(View view, Model model) throws RemoteException {
        this.view = view;
        this.model = model;
//      model.getAllSongs().addAll(clientController.getSongs());
        view.play.setOnAction(this);
        view.pause.setOnAction(this);
    }

    @Override
    public void play() throws RemoteException {
        try {
            method = clientController.getClass().getMethod("play");
            method.invoke(clientController);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() throws RemoteException {
        try {
            method = clientController.getClass().getMethod("pause");
            method.invoke(clientController);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void PlayNextSong() throws RemoteException {
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
        }
    }
}
