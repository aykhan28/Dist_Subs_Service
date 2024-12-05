import java.io.*;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;
import com.google.protobuf.*;

public class AdminHandler implements Runnable {
    private Socket adminSocket;
    private ReentrantLock lock;

    public AdminHandler(Socket adminSocket, ReentrantLock lock) {
        this.adminSocket = adminSocket;
        this.lock = lock;
    }

    public void run() {
        try (InputStream inputStream = adminSocket.getInputStream();
             OutputStream outputStream = adminSocket.getOutputStream()) {

            // Sürekli gelen talepleri dinle
            while (true) {
                // Gelen mesajı Protobuf formatında çöz
                MessageOuterClass.Message request = MessageOuterClass.Message.parseFrom(inputStream);
                if (request == null) {
                    System.out.println("Boş mesaj alındı. Bağlantı kapatılıyor...");
                    break;
                }

                System.out.println("Admin'den gelen talep: " + request.getDemand());

                // Yanıt hazırlayıcı
                MessageOuterClass.Message.Builder responseBuilder = MessageOuterClass.Message.newBuilder()
                        .setDemand(request.getDemand());

                // Talebi işle
                lock.lock();
                try {
                    switch (request.getDemand()) {
                        case STRT: // Başlatma talebi
                            System.out.println("Sunucu başlatıldı.");
                            responseBuilder.setResponse(MessageOuterClass.Message.Response.YEP);
                            break;

                        case CPCTY: // Kapasite bilgisi talebi
                            CapacityOuterClass.Capacity capacity = CapacityOuterClass.Capacity.newBuilder()
                                    .setServerXStatus(1000) // Örnek kapasite
                                    .setTimestamp(System.currentTimeMillis() / 1000L)
                                    .build();
                            System.out.println("Kapasite bilgisi gönderiliyor: " + capacity);
                            // Kapasite bilgisi döner
                            capacity.writeDelimitedTo(outputStream);
                            outputStream.flush();
                            continue; // Bir sonraki mesajı beklemek için döngüye geç
                        
                        default: // Geçersiz talep
                            System.out.println("Geçersiz talep alındı.");
                            responseBuilder.setResponse(MessageOuterClass.Message.Response.NOP);
                            break;
                    }
                } finally {
                    lock.unlock();
                }

                // Yanıtı gönder
                responseBuilder.build().writeDelimitedTo(outputStream);
                outputStream.flush();
            }

        } catch (IOException e) {
            System.err.println("AdminHandler bağlantı hatası: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                adminSocket.close();
                System.out.println("Admin bağlantısı kapatıldı.");
            } catch (IOException e) {
                System.err.println("Admin bağlantısı kapatılamadı: " + e.getMessage());
            }
        }
    }
}
