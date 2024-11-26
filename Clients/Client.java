import java.io.*;
import java.net.*;
import java.util.*;
import com.google.protobuf.*;


public class Client {
    private static final String[] SERVER_HOSTS = {"localhost"};
    private static final int[] SERVER_PORTS = {5001, 5002, 5003};

    public static void main(String[] args) {
        for (int i = 0; i < SERVER_PORTS.length; i++) { // Her server için şimdilik sadece SUBS ve DEL işlemlerini yapıyoruz
            try (Socket socket = new Socket(SERVER_HOSTS[0], SERVER_PORTS[i])) {
                System.out.println("Sunucuya bağlandı: " + SERVER_HOSTS[0] + ":" + SERVER_PORTS[i]);

                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                // Abone olma talebi oluştup gönderme
                SubscriberOuterClass.Subscriber subscriber = SubscriberOuterClass.Subscriber.newBuilder()
                        .setID(i + 1) // Her sunucu için farklı bir ID
                        .setNameSurname("User " + (i + 1))
                        .setStartDate(System.currentTimeMillis() / 1000L)
                        .setLastAccessed(System.currentTimeMillis() / 1000L)
                        .addInterests("sports")
                        .addInterests("technology")
                        .setIsOnline(true)
                        .setDemand(SubscriberOuterClass.Subscriber.Demand.SUBS)
                        .build();

                output.write(subscriber.toByteArray());
                System.out.println("Abone olma talebi gönderildi: " + subscriber);

                // Abonelik iptal talebi
                SubscriberOuterClass.Subscriber unsubscribeRequest = SubscriberOuterClass.Subscriber.newBuilder()
                        .setID(i + 1)
                        .setDemand(SubscriberOuterClass.Subscriber.Demand.DEL)
                        .build();

                output.write(unsubscribeRequest.toByteArray());
                System.out.println("Abonelik iptal talebi gönderildi: " + unsubscribeRequest);

            } catch (IOException e) {
                System.out.println("Sunucuya bağlanılamadı: " + SERVER_HOSTS[0] + ":" + SERVER_PORTS[i]);
                e.printStackTrace();
            }
        }
    }
}
  
