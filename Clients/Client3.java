import java.io.*;
import java.net.*;
import java.util.Scanner;

import com.google.protobuf.*;

public class Client3 {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5003;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("Sunucuya bağlandı: " + SERVER_HOST + ":" + SERVER_PORT);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            System.out.println("Abone ol (A), Aboneliği iptal et (B):");
            Scanner getuserInput = new Scanner(System.in);
            String userInput = getuserInput.nextLine();
            
            if (userInput.equals("A")) {
                SubscriberOuterClass.Subscriber subscriber = SubscriberOuterClass.Subscriber.newBuilder()
                        .setID(5)
                        .setNameSurname("Filiz")
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
            } else if (userInput.equals("B")) {
                SubscriberOuterClass.Subscriber unsubscribeRequest = SubscriberOuterClass.Subscriber.newBuilder()
                        .setID(5)
                        .setDemand(SubscriberOuterClass.Subscriber.Demand.DEL)
                        .build();
                output.write(unsubscribeRequest.toByteArray());
                System.out.println("Abonelik iptal talebi gönderildi: " + unsubscribeRequest);
            }
        } catch (IOException e) {
            System.out.println("Sunucuya bağlanılamadı: " + SERVER_HOST + ":" + SERVER_PORT);
            e.printStackTrace();
        }
    }
}
