import java.io.*;
import java.net.*;
import java.util.List;

public class Server1 {
    private static final int PORT = 5001;
    private static final int ADMIN_PORT = 6001;

    private static Socket server1Socket;
    private static Socket server2Socket;
    private static Socket server3Socket;

    public static void main(String[] args) {
        try (ServerSocket serverSoket = new ServerSocket(PORT);
            ServerSocket adminSoket = new ServerSocket(ADMIN_PORT)) {
            System.out.println("Server1 çalışıyor ve bağlantıları bekliyor...");

            // Admin bağlantısını kabul et
            Socket adminClient = adminSoket.accept();
            new Thread(new AdminHandler(adminClient)).start();

            // Diğer sunuculara bağlan
            new Thread(new DistributedServerHandler(5002, "Server2", server1Socket, server2Socket, server3Socket)).start();
            new Thread(new DistributedServerHandler(5003, "Server3", server1Socket, server2Socket, server3Socket)).start();

            // Kapasite bilgisi gönderici
            new Thread(() -> sendCapacityInfo(adminClient)).start();

            // İstemcileri kabul et
            while (true) {
                Socket clientSoket = serverSoket.accept();
                new Thread(new ClientHandler(clientSoket, "Server1")).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendCapacityInfo(Socket adminClient) {
        try (OutputStream outputStream = adminClient.getOutputStream()) {
            while (true) {
                Thread.sleep(5000); // 5 saniye bekle

                // Kapasite mesajı hazırla
                CapacityOuterClass.Capacity capacity = CapacityOuterClass.Capacity.newBuilder()
                        .setSubscriberCount(CentralRegistry.getAboneListesi().get("Server1").size())
                        .setServerPort(PORT)
                        .build();

                // Mesajı gönder
                capacity.writeTo(outputStream);
                outputStream.flush();
                System.out.println("Kapasite bilgisi admin istemcisine gönderildi: " + capacity);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}