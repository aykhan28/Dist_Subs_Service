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

            while (true) {
                MessageOuterClass.Message request = MessageOuterClass.Message.parseFrom(inputStream);
                if (request == null) {
                    System.out.println("Boş mesaj alındı. Bağlantı kapatılıyor...");
                    break;
                }

                System.out.println("Admin'den gelen talep: " + request.getDemand());
                MessageOuterClass.Message.Builder responseBuilder = MessageOuterClass.Message.newBuilder()
                        .setDemand(request.getDemand());

                lock.lock();
                try {
                    switch (request.getDemand()) {
                        case STRT:
                            System.out.println("Sunucu başlatıldı.");
                            responseBuilder.setResponse(MessageOuterClass.Message.Response.YEP);
                            break;

                        case CPCTY:
                            CapacityOuterClass.Capacity capacity = CapacityOuterClass.Capacity.newBuilder()
                                    .setServerXStatus(1000)
                                    .setTimestamp(System.currentTimeMillis() / 1000L)
                                    .build();
                            System.out.println("Kapasite bilgisi gönderiliyor: " + capacity);
                            capacity.writeTo(outputStream);
                            outputStream.flush();
                            continue;
                        default:
                            System.out.println("Geçersiz talep alındı.");
                            responseBuilder.setResponse(MessageOuterClass.Message.Response.NOP);
                            break;
                    }
                } finally {
                    lock.unlock();
                }

                responseBuilder.build().writeTo(outputStream);
                outputStream.flush();
            }
        } catch (IOException e) {
            System.err.println("AdminHandler bağlantı hatası: " + e.getMessage());
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
