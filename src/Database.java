import java.util.HashMap;
import java.util.Map;


public class Database {
    private Map<String, String> data;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();  // Note: You may add fields to the class and initialize them in here. Do not add parameters!
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean readTryAcquire() {
        // TODO: Add your code here...
    }

    public void readAcquire() {
        // TODO: Add your code here...
    }

    public void readRelease() {
        // TODO: Add your code here...
    }

    public void writeAcquire() {
       // TODO: Add your code here...
    }

    public boolean writeTryAcquire() {
        // TODO: Add your code here...
    }

    public void writeRelease() {
        // TODO: Add your code here...
    }
}