import java.io.*;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;
import com.google.protobuf.*;

public class Server3 {
    private static final int PORT = 5003;
    private static Socket server1Soket;
    private static Socket server2Soket;
    private static boolean basladi = false;
    private static final ReentrantLock kilit = new ReentrantLock();

    public static void main(String[] args) {
        try (ServerSocket serverSoket = new ServerSocket(PORT)) {
            System.out.println("Server3 bağlantilari kabul etmeye hazir");

            // Sunucu1 ve Sunucu2'ye bağlantı
            new Thread(new ServerBaglanti(5001, "Server1")).start();
            new Thread(new ServerBaglanti(5002, "Server2")).start();

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
                    if (serverAdi.equals("Server1")) {
                        server1Soket = new Socket("localhost", port);
                    } else if (serverAdi.equals("Server2")) {
                        server2Soket = new Socket("localhost", port);
                    }
                    System.out.println("Server3 " + serverAdi + " ile bağlanti kurdu");
                    break;
                } catch (IOException e) {
                    System.out.println("Server3 " + serverAdi + " ile bağlanti kurmaya çalisiyor");
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
            try {
                InputStream giris = clientSoket.getInputStream();
                OutputStream cikis = clientSoket.getOutputStream();

                MessageOuterClass.Message mesaj = MessageOuterClass.Message.parseFrom(giris);
                System.out.println("Server3 aldi: " + mesaj.getDemand());

                MessageOuterClass.Message.Builder yanitOlustur = MessageOuterClass.Message.newBuilder()
                        .setDemand(mesaj.getDemand());

                if (mesaj.getDemand() == MessageOuterClass.Message.Demand.STRT) {
                    basladi = true;
                    yanitOlustur.setResponse(MessageOuterClass.Message.Response.YEP);
                } else if (mesaj.getDemand() == MessageOuterClass.Message.Demand.CPCTY && basladi) {
                    CapacityOuterClass.Capacity kapasite = CapacityOuterClass.Capacity.newBuilder()
                            .setServerXStatus(1000)
                            .setTimestamp(System.currentTimeMillis() / 1000L)
                            .build();
                    cikis.write(kapasite.toByteArray());
                    return;
                } else {
                    yanitOlustur.setResponse(MessageOuterClass.Message.Response.NOP);
                }

                MessageOuterClass.Message yanit = yanitOlustur.build();
                cikis.write(yanit.toByteArray());

                kilit.lock();
                try {
                    if (server1Soket != null && !server1Soket.isClosed()) {
                        server1Soket.getOutputStream().write(yanit.toByteArray());
                    }
                    if (server2Soket != null && !server2Soket.isClosed()) {
                        server2Soket.getOutputStream().write(yanit.toByteArray());
                    }
                } finally {
                    kilit.unlock();
                }

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
                try {
                    clientSoket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
