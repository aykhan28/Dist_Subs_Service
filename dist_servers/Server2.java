package dist_servers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.protos.Capacity;
import com.protos.Configuration;
import com.protos.Demand;
import com.protos.Message;
import com.protos.MethodType;
import com.protos.Response;
import com.protos.Subscriber;

public class Server2 {
    private int serverId = 2;
    private int toleranceLevel;
    private boolean isCanStart = false;
    private volatile boolean serverConnectionTime = true;

    private ServerSocket serverSocket;
    private List<Socket> serverSockets = new ArrayList<>();
    private Map<Long, Subscriber> clientsData = new TreeMap<>();

    public static void main(String[] args) {
        Server2 server = new Server2();
        try {
            server.start();
        } catch (IOException e) {
            System.out.println("Sunucu başlatılamadı.");
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(5002);
        connectAdmin();
    }

    private void connectOtherServers(int toleranceLevel) {
        int[] ports = {5001, 5002, 5003};
        for (int port : ports) {
            if (port == 5002) continue;
            if (toleranceLevel > 0) {
                new Thread(() -> sendServerSocket(port)).start();
            } else {
                break;
            }
            toleranceLevel--;
        }

        Thread thread = new Thread(this::acceptServerSocket);
        thread.start();
        try {
            thread.join(2000);
            if (thread.isAlive()) {
                serverConnectionTime = false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendServerSocket(int port) {
        try {
            serverSockets.add(new Socket("localhost", port));
            System.out.println("Diğer sunucuya bağlanıldı.");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptServerSocket() {
        try {
            serverSocket.setSoTimeout(1000);
            while (serverConnectionTime) {
                Socket socket = serverSocket.accept();
                System.out.println("Bir sunucu bağlandı.");
                new Thread(() -> handleServer(socket)).start();
            }
        } catch (IOException e) {
            try {
                serverSocket.setSoTimeout(0);
                System.out.println("Diğer sunuculara bağlanma süresi doldu.");
            } catch (SocketException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void handleServer(Socket socket) {
        try {
            while (isCanStart) {
                Subscriber sub = Subscriber.parseFrom(receiveData(socket));
                addClient(sub);
            }
        } catch (IOException e) {
            System.out.println("Sunucu bağlantısı koptu.");
            e.printStackTrace();
        }
    }

    private synchronized void sendToServer(Subscriber subscriber) throws IOException {
        for (Socket socket : serverSockets) {
            sendData(subscriber, socket);
        }
    }

    private synchronized void addClient(Subscriber subscriber) {
        clientsData.put(subscriber.getID(), subscriber);
    }

    private synchronized void removeClient(Long subId) {
        clientsData.remove(subId);
    }

    private void handleClient(Socket clientSocket) throws IOException {
        Subscriber sub = Subscriber.parseFrom(receiveData(clientSocket));
        if (clientsData.get(sub.getID()) != sub) {
            switch (sub.getDemand()) {
                case SUBS:
                    addClient(sub);
                    System.out.println("Başarıyla abone olundu. Abone ID: " + sub.getID());
                    break;
                case DEL:
                    removeClient(sub.getID());
                    System.out.println("Abone başarıyla silindi. Abone ID: " + sub.getID());
                    break;
                default:
                    System.out.println("Geçersiz istek.");
                    break;
            }
            sendToServer(sub);
            System.out.println("Toplam Abone Sayısı: " + clientsData.size());
        }
    }

    private void connectClients() {
        new Thread(() -> {
            while (isCanStart) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Yeni bir istemci bağlandı: " + clientSocket.getRemoteSocketAddress());
                    new Thread(() -> {
                        try {
                            handleClient(clientSocket);
                        } catch (IOException e) {
                            System.out.println("İstemci bağlantı hatası.");
                        }
                    }).start();
                } catch (Exception e) {
                    System.out.println("Bağlantı kurulamadı.");
                }
            }
        }).start();
    }

    private void connectAdmin() {
        try (Socket clientSocket = serverSocket.accept()) {
            System.out.println("Admin bağlandı.");
            Configuration conf = Configuration.parseFrom(receiveData(clientSocket));
            Message message = createMessage(conf);
            sendData(message, clientSocket);
            handleAdmin(clientSocket);
        } catch (IOException | InterruptedException e) {
            System.out.println("Admin bağlantısı başarısız. Sunucu kapatılıyor.");
            System.exit(1);
        }
    }

    private void handleAdmin(Socket adminSocket) throws IOException, InterruptedException {
        if (isCanStart) {
            connectOtherServers(toleranceLevel);
            Thread.sleep(1000);
            connectClients();
            System.out.println("Admin taleplerine hazır.");
            while (isCanStart) {
                Message message = Message.parseFrom(receiveData(adminSocket));
                Capacity capacity = Capacity.parseFrom(receiveData(adminSocket));
                if (message.getDemand() == Demand.CPCTY && capacity.getServerId() == serverId) {
                    sendData(createCapacity(), adminSocket);
                }
            }
        }
    }

    private static <T extends MessageLite> void sendData(T response, Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        byte[] responseBytes = response.toByteArray();
        output.writeInt(responseBytes.length);
        output.write(responseBytes);
    }

    private static byte[] receiveData(Socket socket) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        int length = input.readInt();
        byte[] requestBytes = new byte[length];
        input.readFully(requestBytes);
        return requestBytes;
    }

    private Message createMessage(Configuration configuration) {
        toleranceLevel = configuration.getFaultToleranceLevel();
        isCanStart = configuration.getMethodType() == MethodType.STRT;
        return Message.newBuilder().setDemand(Demand.STRT).setResponse(isCanStart ? Response.YEP : Response.NOP).build();
    }

    private Capacity createCapacity() {
        return Capacity.newBuilder()
                .setServerXStatus(clientsData.size())
                .setTimestamp(System.currentTimeMillis() / 1000)
                .setServerId(serverId)
                .build();
    }
}