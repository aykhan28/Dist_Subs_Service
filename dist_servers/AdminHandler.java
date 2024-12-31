import java.io.*;
import java.net.*;

public class AdminHandler implements Runnable {
    private Socket adminSocket;

    public AdminHandler(Socket adminSocket) {
        this.adminSocket = adminSocket;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(adminSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(adminSocket.getOutputStream(), true)) {
            
            System.out.println("Admin istemcisi bağlı.");

            while (true) {
                String incomingMessage = reader.readLine();
                if (incomingMessage == null || incomingMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Admin istemcisi bağlantıyı kapattı.");
                    break;
                }
                System.out.println("Admin'den mesaj alındı: " + incomingMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                adminSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
