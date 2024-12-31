import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class CentralRegistry {
    private static final Map<String, List<SubscriberOuterClass.Subscriber>> aboneListesi = new HashMap<>();
    private static final ReentrantLock kilit = new ReentrantLock();

    static {
        aboneListesi.put("Server1", new ArrayList<>());
        aboneListesi.put("Server2", new ArrayList<>());
        aboneListesi.put("Server3", new ArrayList<>());
    }

    public static void addSubscriber(String serverName, SubscriberOuterClass.Subscriber subscriber) {
        kilit.lock();
        try {
            aboneListesi.get(serverName).add(subscriber);
            //System.out.println("Abone eklendi: Server=" + serverName + ", ID=" + subscriber.getID());
        } finally {
            kilit.unlock();
        }
    }

    public static void removeSubscriber(String serverName, int subscriberId) {
        kilit.lock();
        try {
            aboneListesi.get(serverName).removeIf(subscriber -> subscriber.getID() == subscriberId);
            //System.out.println("Abone kaldırıldı: Server=" + serverName + ", ID=" + subscriberId);
        } finally {
            kilit.unlock();
        }
    }

    public static Map<String, List<SubscriberOuterClass.Subscriber>> getAboneListesi() {
        kilit.lock();
        try {
            return new HashMap<>(aboneListesi); // Koruma için kopya döner
        } finally {
            kilit.unlock();
        }
    }
}
