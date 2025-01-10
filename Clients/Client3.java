package Clients;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import com.protos.DemandType;
import com.protos.Subscriber;

public class Client3 {

    private static final String HOST = "localhost";
    private static final int[] PORTS = {5001, 5002, 5003};
    private Socket socket;
    private int Id = 5;
    private String nameSurname = "Filiz";
    private String demand;
    private String[] AllInterests = {"sports", "technology"};
    private int serverNo = 3;

    public Client3 connectToServer() throws IOException {
        socket = new Socket(HOST, PORTS[serverNo - 1]);
        System.out.println("Sunucu" + serverNo + "'ye başarıyla bağlanıldı...");
        return this;
    }

    public void sendSubscriptionRequest() throws IOException {
        Subscriber subscriber = createSubscriptionMessage();
        transmitMessage(subscriber);
        System.out.println("Abone bilgisi sunucuya gönderildi:\n" + subscriber);
    }

    private Subscriber createSubscriptionMessage() {
        return Subscriber.newBuilder()
                .setNameSurname(nameSurname)
                .setStartDate(System.currentTimeMillis())
                .setLastAccessed(System.currentTimeMillis())
                .addAllInterests(Arrays.asList(AllInterests))
                .setIsOnline(true)
                .setDemand(convertDemandType(demand))
                .setID(Id)
                .build();
    }

    private DemandType convertDemandType(String demand) {
        switch (demand) {
            case "SUB":
                return DemandType.SUBS;
            case "DEL":
                return DemandType.DEL;
            default:
                throw new IllegalArgumentException("Geçersiz istek");
        }
    }

    private void transmitMessage(Subscriber subscriber) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        byte[] responseBytes = subscriber.toByteArray();
        output.writeInt(responseBytes.length);
        output.write(responseBytes);
        output.flush();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Abone ol (A), Aboneliği iptal et (B):");
        String userInput = scanner.nextLine().toUpperCase();

        Client3 client = new Client3();

        if (userInput.equals("A")) {
            client.demand = "SUB";
        } else if (userInput.equals("B")) {
            client.demand = "DEL";
        } else {
            System.out.println("Geçersiz giriş. Lütfen 'A' veya 'B' girin.");
            scanner.close();
            return;
        }

        try {
            client.connectToServer();
            client.sendSubscriptionRequest();
        } catch (IOException e) {
            System.err.println("Sunucuya bağlanırken bir hata oluştu.");
            e.printStackTrace();
        }

        scanner.close();
    }
}