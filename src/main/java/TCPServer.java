import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(5020);

            while (true) {
                Socket socket = server.accept();
                new TCPClientThread();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
