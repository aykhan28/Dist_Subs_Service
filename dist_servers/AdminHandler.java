import java.io.*;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;
import com.google.protobuf.*;

public class AdminHandler implements Runnable {
    private Socket clientSoket;
    private ReentrantLock kilit;

    public AdminHandler(Socket clientSoket, ReentrantLock kilit) {
        this.clientSoket = clientSoket;
        this.kilit = kilit;
    }

    public void run() {
        try (InputStream giris = clientSoket.getInputStream();
             OutputStream cikis = clientSoket.getOutputStream()) {

            while (true) {
                MessageOuterClass.Message mesaj = MessageOuterClass.Message.parseFrom(giris);
                MessageOuterClass.Message.Builder yanitOlustur = MessageOuterClass.Message.newBuilder()
                        .setDemand(mesaj.getDemand());

                kilit.lock();
                try {
                    switch (mesaj.getDemand()) {
                        case STRT:
                            System.out.println("Sunucu başlatıldı.");
                            yanitOlustur.setResponse(MessageOuterClass.Message.Response.YEP);
                            break;

                        case CPCTY:
                            CapacityOuterClass.Capacity kapasite = CapacityOuterClass.Capacity.newBuilder()
                                    .setServerXStatus(1000) // Örnek kapasite
                                    .setTimestamp(System.currentTimeMillis() / 1000L)
                                    .build();
                            cikis.write(kapasite.toByteArray());
                            cikis.flush();
                            System.out.println("Kapasite bilgisi gönderildi.");
                            break;

                        default:
                            yanitOlustur.setResponse(MessageOuterClass.Message.Response.NOP);
                            break;
                    }
                } finally {
                    kilit.unlock();
                }
                cikis.flush();
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
