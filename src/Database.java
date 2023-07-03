import java.util.HashMap;
import java.util.Map;

/**
 * A class that represents a Database
 * This class allowing concurrent read and write of data
 */
public class Database {
    private Map<String, String> data;
    private final int k;
    private boolean isPut = false;
    private int activeGet = 0;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();
        this.k = maxNumOfReaders;
    }

    /**
     * Inserts a key, and it's value, to the database
     *
     * @param key the key that associated with the value
     * @param value the value that stored in the database
     */
    public void put(String key, String value) {
        data.put(key, value);
    }

    /**
     * Gets the value that associated with the provided key from the database
     *
     * @param key the key that it's value retrieved
     * @return the value that associated with the key if the key is found, else - null
     */
    public String get(String key) {
        return data.get(key);
    }

    /**
     * tries to acquire a read lock without blocking
     * If the attempt was successful, so Raises the count of active readers by one
     *
     * @return true if the read lock is acquired, else - false
     */
    public boolean readTryAcquire() {
        // TODO: Add your code here...
        synchronized (this) {
            if (activeGet < k) {
                if(!isPut){
                    activeGet++;
                    return true;
                }
            } else {
                return false;
            }

            return false;
        }
    }

    /**
     * Acquires a lock to read
     * Until the lock is realised, the reading is blocked
     *
     * @throws RuntimeException if the current thread that reading is being interrupted
     */
    public void readAcquire() {
        // TODO: Add your code here...
        synchronized (this) {
            boolean flag = isPut || activeGet >= k;
            while (isPut || activeGet >= k) {
                flag = isPut || activeGet >= k;
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            activeGet++;
        }
    }

    /**
     * Releases the lock for reading
     * notifies threads that are waiting
     *
     * @throws IllegalMonitorStateException if there is no thread that is reading at the moment
     */
    public void readRelease() {
        // TODO: Add your code here...
        synchronized (this) {
            if (!(activeGet > 0)) {
                throw new IllegalMonitorStateException("Illegal read release attempt");
            } else {
                activeGet--;
                if (activeGet == 0) {
                    notifyAll();
                }
            }
        }
    }

    /**
     * Acquires a lock to write
     * Until the lock is realised, the writing is blocked
     *
     * @throws RuntimeException if the current thread that reading is being interrupted
     */
    public void writeAcquire() {
        // TODO: Add your code here...
        synchronized (this) {
            boolean flag = isPut || activeGet > 0;
            while (isPut || activeGet > 0) {
                flag = isPut || activeGet > 0;
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            isPut = true;
        }
    }

    /**
     * tries to acquire a write lock without blocking
     * If the attempt was successful, so Raises the count of active writers by one
     *
     * @return true if the write lock is acquired, else - false
     */
    public boolean writeTryAcquire() {
        // TODO: Add your code here...
        synchronized (this) {
            if (activeGet == 0) {
                if(!isPut) {
                    isPut = true;
                    return true;
                }
            } else {
                return false;
            }

            return false;
        }
    }

    /**
     * Releases the lock for writing
     * notifies threads that are waiting
     *
     * @throws IllegalMonitorStateException if there is no thread that is reading at the moment
     */
    public void writeRelease() {
        // TODO: Add your code here...
        synchronized (this) {
            if (!isPut) {
                throw new IllegalMonitorStateException("Illegal write release attempt");
            } else {
                isPut = false;
                notifyAll();
            }
        }
    }
}