import javafx.application.Platform;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UDPClient extends Thread {

    View view;
    UDPClient(View view) {
        this.view = view;
    }

    @Override
    public void run(){
        try {
            DatagramSocket socket = new DatagramSocket(5002);
            while (true) {
                InetAddress address = InetAddress.getByName("localhost");
                String command = "{\"cmd\":\"time\"}";
                byte[] buffer = command.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 5000);
                socket.send(packet);
                byte[] answer = new byte[14];
                packet = new DatagramPacket(answer, answer.length);
                socket.receive(packet);
                String answerString = new String(packet.getData());
                Platform.runLater(() -> view.timel.setText(answerString));
                Thread.sleep(100);
            }
        } catch (SocketException e) {
                e.printStackTrace();
        } catch (UnknownHostException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
    }
}
