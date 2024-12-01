import java.io.*;
import java.net.*;
import com.google.protobuf.*;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5001;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("Sunucuya bağlandı: " + SERVER_HOST + ":" + SERVER_PORT);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            // Talep kimligi
            PrintWriter writer = new PrintWriter(output, true);
            writer.println("SUBSCRIBER");

            // Abone olma talebi
            SubscriberOuterClass.Subscriber subscriber = SubscriberOuterClass.Subscriber.newBuilder()
                    .setID(1)
                    .setNameSurname("Beyza")
                    .setStartDate(System.currentTimeMillis() / 1000L)
                    .setLastAccessed(System.currentTimeMillis() / 1000L)
                    .addInterests("sports")
                    .addInterests("technology")
                    .setIsOnline(true)
                    .setDemand(SubscriberOuterClass.Subscriber.Demand.SUBS)
                    .build();
            subscriber.writeTo(output);
            output.flush();
            System.out.println("Abone olma talebi gönderildi: " + subscriber);

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String serverResponse = reader.readLine();
            System.out.println("Sunucudan gelen yanıt: " + serverResponse);
        } catch (IOException e) {
            System.out.println("Sunucuya bağlanılamadı: " + SERVER_HOST + ":" + SERVER_PORT);
            e.printStackTrace();
        }
    }
}