import java.io.*;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;
import com.google.protobuf.*;

public class Server1 {
    private static final int PORT = 5001;
    private static Socket server2Soket;
    private static Socket server3Soket;
    private static boolean basladi = false;
    private static ReentrantLock kilit = new ReentrantLock();

    public static void main(String[] args) {
        try (ServerSocket serverSoket = new ServerSocket(PORT)) {
            System.out.println("Server1 çalışıyor ve bağlantıları bekliyor...");

            new Thread(new ServerBaglanti(5002, "Server2")).start();
            new Thread(new ServerBaglanti(5003, "Server3")).start();

            while (true) {
                Socket clientSoket = serverSoket.accept();
                new Thread(new ClientIsleyici(clientSoket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ServerBaglanti implements Runnable {
        private int port;
        private String serverAdi;

        public ServerBaglanti(int port, String serverAdi) {
            this.port = port;
            this.serverAdi = serverAdi;
        }

        public void run() {
            while (true) {
                try {
                    if (serverAdi.equals("Server2")) {
                        server2Soket = new Socket("localhost", port);
                    } else if (serverAdi.equals("Server3")) {
                        server3Soket = new Socket("localhost", port);
                    }
                    System.out.println("Server1 " + serverAdi + " ile bağlantı kurdu");
                    break;
                } catch (IOException e) {
                    System.out.println("Server1 " + serverAdi + " ile bağlantı kurmaya çalışıyor");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    static class ClientIsleyici implements Runnable {
        private Socket clientSoket;

        public ClientIsleyici(Socket clientSoket) {
            this.clientSoket = clientSoket;
        }

        public void run() {
            kilit.lock();
            try {
                InputStream giris = clientSoket.getInputStream();
                OutputStream cikis = clientSoket.getOutputStream();

                // Mesaj
                MessageOuterClass.Message mesaj = MessageOuterClass.Message.parseFrom(giris);
                System.out.println("Server1 aldı: " + mesaj.getDemand());

                // Yanıt
                MessageOuterClass.Message.Builder yanitOlustur = MessageOuterClass.Message.newBuilder()
                        .setDemand(mesaj.getDemand());

                if (mesaj.getDemand() == MessageOuterClass.Message.Demand.STRT) {
                    basladi = true;
                    yanitOlustur.setResponse(MessageOuterClass.Message.Response.YEP);
                } else if (mesaj.getDemand() == MessageOuterClass.Message.Demand.CPCTY && basladi) {
                    // Kapasite mesajı
                    CapacityOuterClass.Capacity kapasite = CapacityOuterClass.Capacity.newBuilder()
                            .setServerXStatus(1000) // Örn sunuu durumu
                            .setTimestamp(System.currentTimeMillis() / 1000L)
                            .build();
                    cikis.write(kapasite.toByteArray());
                    return;
                } else {
                    yanitOlustur.setResponse(MessageOuterClass.Message.Response.NOP);
                }

                // Yanıtı gönderme
                MessageOuterClass.Message yanit = yanitOlustur.build();
                cikis.write(yanit.toByteArray());

                // Diğer sunuculara yanıtı iletiyoruz
                if (server2Soket != null && !server2Soket.isClosed()) {
                    server2Soket.getOutputStream().write(yanit.toByteArray());
                }
                if (server3Soket != null && !server3Soket.isClosed()) {
                    server3Soket.getOutputStream().write(yanit.toByteArray());
                }

                // Subs taleplerini işleme
                SubscriberOuterClass.Subscriber abone = SubscriberOuterClass.Subscriber.parseFrom(giris);
                switch (abone.getDemand()) {
                    case SUBS:
                        System.out.println("Yeni abone: " + abone.getNameSurname());
                        break;
                    case DEL:
                        System.out.println("Abonelik iptali talebi: " + abone.getID());
                        break;
                    default:
                        System.out.println("Geçersiz istek");
                        break;
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
    }
}
