import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;

public class Task3 {
    public static void main(String[] args) {
        Mapper mapper = new Mapper();

        new Thread(() -> {
            for (int i = 0; i < 1_000; i++) {
                mapper.putToHashMap(i, i + 50);
            }
        }).start();

        new Thread(() -> {
            for (int i = 1_000; i < 2_000; i++) {
                mapper.putToHashMap(i, i + 50);
            }
        }).start();

        new Thread(() -> {
            for (int i = 2_000; i < 3_000; i++) {
                mapper.putToHashMap(i, i + 50);
            }
        }).start();

        new Thread(mapper::readFromMap).start();


    }
}

class Mapper {
    //private final HashMap<Integer, Integer> hashMap = new HashMap<>(3000);
    private ConcurrentHashMap<Integer, Integer> hashMap = new ConcurrentHashMap<>(3000);

    void putToHashMap(Integer number, Integer id) {
        hashMap.put(number, id);
    }

    void readFromMap() {
        Iterator<Integer> iterator = hashMap.keySet().iterator();
        synchronized (hashMap) {
            while (iterator.hasNext()) {
                System.out.println(hashMap.get(iterator.next()));
            }
        }
    }
}

