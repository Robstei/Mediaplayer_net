import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClientThread extends Thread {

    View view;
    private DatagramPacket packet;
    private DatagramSocket socket;

    public UDPClientThread(DatagramPacket packet, DatagramSocket socket, View view) {
        this.packet = packet;
        this.socket = socket;
        this.view = view;

    }

    @Override
    public void run() {
        int port = packet.getPort();
        byte[] data = packet.getData();
        InetAddress address = packet.getAddress();

        String dataString = new String(data);
        Scanner scanner = new Scanner(dataString);
        String keyword = scanner.next();
        byte[] answer = new byte[1];

        if (keyword.equals("{\"cmd\":\"time\"}")) {
            answer = view.timel.getText().getBytes();
            packet = new DatagramPacket(answer, answer.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                socket.send(new DatagramPacket(answer, answer.length, address, port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
