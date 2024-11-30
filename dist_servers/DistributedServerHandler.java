import java.io.IOException;
import java.net.Socket;

class DistributedServerHandler implements Runnable {
    private int port;
    private String serverAdi;
    private Socket server1Socket;
    private Socket server2Socket;
    private Socket server3Socket;

    public DistributedServerHandler(int port, String serverAdi, Socket server1Socket, Socket server2Socket, Socket server3Socket) {
        this.port = port;
        this.serverAdi = serverAdi;
        this.server1Socket = server1Socket;
        this.server2Socket = server2Socket;
        this.server3Socket = server3Socket;
    }

    public void run() {
        while (true) {
            try {
                if (serverAdi.equals("Server1")) {
                    server1Socket = new Socket("localhost", port);
                } else if (serverAdi.equals("Server2")) {
                    server2Socket = new Socket("localhost", port);
                } else if (serverAdi.equals("Server3")) {
                    server3Socket = new Socket("localhost", port);
                }
                System.out.println(serverAdi + " ile bağlantı kurdu");
                break;
            } catch (IOException e) {
                System.out.println(serverAdi + " ile bağlantı kurmaya çalışıyor");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}