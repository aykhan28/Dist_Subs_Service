import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import com.google.protobuf.*;

public class ClientHandler implements Runnable {
    private Socket clientSoket;
    private List<SubscriberOuterClass.Subscriber> aboneListesi;
    private ReentrantLock kilit;

    public ClientHandler(Socket clientSoket, List<SubscriberOuterClass.Subscriber> aboneListesi, ReentrantLock kilit) {
        this.clientSoket = clientSoket;
        this.aboneListesi = aboneListesi;
        this.kilit = kilit;
    }

    public void run() {
        try {
            InputStream giris = clientSoket.getInputStream();
            OutputStream cikis = clientSoket.getOutputStream();

            SubscriberOuterClass.Subscriber abone = SubscriberOuterClass.Subscriber.parseFrom(giris);
            String yanitMesaji;

            kilit.lock();
            try {
                switch (abone.getDemand()) {
                    case SUBS:
                        aboneListesi.add(abone);
                        System.out.println("Yeni abone: ID=" + abone.getID() + ", Ad=" + abone.getNameSurname());
                        yanitMesaji = "Abonelik başarılı: " + abone.getNameSurname();
                        break;

                    case DEL:
                        aboneListesi.removeIf(a -> a.getID() == abone.getID());
                        System.out.println("Abonelik iptali talebi: ID=" + abone.getID());
                        yanitMesaji = "Abonelik iptal edildi: ID=" + abone.getID();
                        break;

                    default:
                        yanitMesaji = "Geçersiz talep.";
                }
            } finally {
                kilit.unlock();
            }

            cikis.write(yanitMesaji.getBytes());
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
