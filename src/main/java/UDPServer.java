import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer extends Thread{

    View view;
    DatagramSocket socket;

    public UDPServer(View view) {
            this.view = view;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(5000);

            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[14], 14);
                socket.receive(packet);
                new UDPClientThread(packet, socket, view).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}