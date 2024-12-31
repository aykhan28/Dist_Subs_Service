import java.io.*;
import java.net.*;

public class Server3 {
    private static final int PORT = 5003;
    private static final int ADMIN_PORT = 6003;

    private static Socket server1Socket;
    private static Socket server2Socket;
    private static Socket server3Socket;

    public static void main(String[] args) {
        try (ServerSocket serverSoket = new ServerSocket(PORT);
            ServerSocket adminSoket = new ServerSocket(ADMIN_PORT)) {
            System.out.println("Server3 çalışıyor ve bağlantıları bekliyor...");

            Socket adminClient = adminSoket.accept();
            new Thread(new AdminHandler(adminClient)).start();

            new Thread(new DistributedServerHandler(5001, "Server1", server1Socket, server2Socket, server3Socket)).start();
            new Thread(new DistributedServerHandler(5002, "Server2", server1Socket, server2Socket, server3Socket)).start();

            new Thread(() -> sendCapacityInfo(adminClient)).start();

            while (true) {
                Socket clientSoket = serverSoket.accept();
                new Thread(new ClientHandler(clientSoket, "Server3")).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendCapacityInfo(Socket adminClient) {
        try (OutputStream outputStream = adminClient.getOutputStream()) {
            while (true) {
                Thread.sleep(5000);

                CapacityOuterClass.Capacity capacity = CapacityOuterClass.Capacity.newBuilder()
                        .setSubscriberCount(CentralRegistry.getAboneListesi().get("Server3").size())
                        .setServerPort(PORT)
                        .build();

                capacity.writeTo(outputStream);
                outputStream.flush();
                System.out.println("Kapasite bilgisi admin istemcisine gönderildi: " + capacity);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}