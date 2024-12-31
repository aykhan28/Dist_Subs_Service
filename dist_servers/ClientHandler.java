
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

import com.google.protobuf.*;

public class ClientHandler implements Runnable {

    private Socket clientSoket;
    private String serverName;

    public ClientHandler(Socket clientSoket, String serverName) {
        this.clientSoket = clientSoket;
        this.serverName = serverName;
    }

    public void run() {
        try (InputStream giris = clientSoket.getInputStream(); OutputStream cikis = clientSoket.getOutputStream()) {

            SubscriberOuterClass.Subscriber abone = SubscriberOuterClass.Subscriber.parseFrom(giris);

            switch (abone.getDemand()) {
                case SUBS:
                    CentralRegistry.addSubscriber(serverName, abone);
                    break;

                case DEL:
                    CentralRegistry.removeSubscriber(serverName, abone.getID());
                    break;

                default:
                    System.out.println("Geçersiz talep alındı.");
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
