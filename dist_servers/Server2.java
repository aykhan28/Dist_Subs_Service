import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Server2 {
    private static final int PORT = 5002;
    private static final int ADMIN_PORT = 6002;
    private static ReentrantLock kilit = new ReentrantLock();
    private static List<SubscriberOuterClass.Subscriber> aboneListesi = new ArrayList<>();
    private static Socket server1Socket;
    private static Socket server2Socket;
    private static Socket server3Socket;

    public static void main(String[] args) {
        try (ServerSocket serverSoket = new ServerSocket(PORT);
            ServerSocket adminSoket = new ServerSocket(ADMIN_PORT)) {
            System.out.println("Server2 çalışıyor ve bağlantıları bekliyor...");

            Socket adminClient = adminSoket.accept();
            new Thread(new AdminHandler(adminClient, kilit)).start();

            new Thread(new DistributedServerHandler(5001, "Server1", server1Socket, server2Socket, server3Socket)).start();
            new Thread(new DistributedServerHandler(5003, "Server3", server1Socket, server2Socket, server3Socket)).start();

            while (true) {
                Socket clientSoket = serverSoket.accept();
                new Thread(new ClientHandler(clientSoket, aboneListesi, kilit)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}