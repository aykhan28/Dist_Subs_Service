import java.io.*;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;

import com.google.protobuf.*;

public class AdminHandler implements Runnable {
    private Socket adminSocket;

    public AdminHandler(Socket adminSocket) {
        this.adminSocket = adminSocket;
    }

    public void run() {
        try (InputStream inputStream = adminSocket.getInputStream();
             OutputStream outputStream = adminSocket.getOutputStream()) {

            // İlk mesajı oku (STRT)
            MessageOuterClass.Message request = MessageOuterClass.Message.parseFrom(inputStream);
            if (request == null || request.getDemand() != MessageOuterClass.Message.Demand.STRT) {
                System.out.println("Geçersiz başlangıç talebi alındı. Bağlantı kapatılıyor...");
                return;
            }

            System.out.println("Sunucu başlatılıyor...");
            MessageOuterClass.Message.Builder responseBuilder = MessageOuterClass.Message.newBuilder()
                    .setDemand(request.getDemand())
                    .setResponse(MessageOuterClass.Message.Response.YEP);

            responseBuilder.build().writeTo(outputStream);
            outputStream.flush();

            while (true) {
                request = MessageOuterClass.Message.parseFrom(inputStream);
                if (request == null || request.getDemand() != MessageOuterClass.Message.Demand.CPCTY) {
                    System.out.println("Geçersiz kapasite talebi alındı. Bağlantı kapatılıyor...");
                    return;
                }

                CapacityOuterClass.Capacity capacity = CapacityOuterClass.Capacity.newBuilder()
                        .setServerXStatus(1000)
                        .setTimestamp(System.currentTimeMillis() / 1000L)
                        .build();
                System.out.println("Kapasite bilgisi gönderiliyor: " + capacity);

                capacity.writeTo(outputStream);
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