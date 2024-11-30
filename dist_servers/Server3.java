import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Server3 {
    private static final int PORT = 5003;
    private static ReentrantLock kilit = new ReentrantLock();
    private static List<SubscriberOuterClass.Subscriber> aboneListesi = new ArrayList<>();
    private static Socket server1Socket;
    private static Socket server2Socket;
    private static Socket server3Socket;

    public static void main(String[] args) {
        try (ServerSocket serverSoket = new ServerSocket(PORT)) {
            System.out.println("Server1 çalışıyor ve bağlantıları bekliyor...");

            new Thread(new DistributedServerHandler(5001, "Server1", server1Socket, server2Socket, server3Socket)).start();
            new Thread(new DistributedServerHandler(5002, "Server2", server1Socket, server2Socket, server3Socket)).start();

            while (true) {
                Socket clientSoket = serverSoket.accept();
                kilit.lock();
                try {
                    InputStream giris = clientSoket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(giris));

                    String clientType = reader.readLine();
                    System.out.println("Gelen istemci türü: " + clientType);

                    if ("SUBSCRIBER".equalsIgnoreCase(clientType)) {
                        new Thread(new ClientHandler(clientSoket, aboneListesi, kilit)).start();;
                    } else if ("ADMIN".equalsIgnoreCase(clientType)) {
                        new Thread(new AdminHandler(clientSoket, kilit)).start();
                    } else {
                        System.out.println("Bilinmeyen istemci türü: " + clientType);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    kilit.unlock();
                    try {
                        clientSoket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}