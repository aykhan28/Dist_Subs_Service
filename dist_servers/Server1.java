import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Server1 {
    private static final int PORT = 5001;
    private static final int ADMIN_PORT = 6001;
    private static ReentrantLock kilit = new ReentrantLock();
    private static List<SubscriberOuterClass.Subscriber> aboneListesi = new ArrayList<>();
    private static Socket server1Socket;
    private static Socket server2Socket;
    private static Socket server3Socket;

    public static void main(String[] args) {
        try (ServerSocket serverSoket = new ServerSocket(PORT);
            ServerSocket adminSoket = new ServerSocket(ADMIN_PORT)) {
            System.out.println("Server1 çalışıyor ve bağlantıları bekliyor...");

            Socket adminClient = adminSoket.accept();
            new Thread(new AdminHandler(adminClient, kilit)).start();

            new Thread(new DistributedServerHandler(5002, "Server2", server1Socket, server2Socket, server3Socket)).start();
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