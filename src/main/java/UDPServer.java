import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPServer extends Thread{

    View view;

    public UDPServer(View view) {
        this.view = view;
    }

    @Override
    public void run(){
        try (DatagramSocket socket = new DatagramSocket(5000);) {
            while (true) {

                DatagramPacket packet = new DatagramPacket(new byte[14], 14);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                int len = packet.getLength();
                byte[] data = packet.getData();
                String dataString  = new String(data);

                Scanner scanner = new Scanner(dataString);
                String keyword = scanner.next();
                byte[] answer = new byte[1];
                System.out.println(keyword);
                if (keyword.equals("{\"cmd\":\"time\"}")) {
                    System.out.println(view.timel.getText());
                    System.out.println(view.timel.getText().getBytes());
                    answer = view.timel.getText().getBytes();
                    packet = new DatagramPacket(answer, answer.length,address,port);
                    socket.send(packet);
                } else {
                    socket.send(new DatagramPacket(answer,answer.length,address,port));
                }
                Thread.sleep(10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
