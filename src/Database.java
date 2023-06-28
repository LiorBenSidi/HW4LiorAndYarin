import java.util.HashMap;
import java.util.Map;

/**
 * A class that represents a Database
 * This class allowing concurrent read and write of data
 */
public class Database {
    private Map<String, String> data;
    private int k;
    private boolean isPut;
    private int activeGet;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();
        this.k = maxNumOfReaders;
        this.isPut = false;
        this.activeGet = 0;
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
            if (!isPut && activeGet < k) {
                activeGet++;
                return true;
            }
            return false;
        }
    }

    /**
     * Acquires a read lock, blocking until it can be obtained.
     *
     * @throws RuntimeException if the current thread is interrupted while waiting.
     */
    public void readAcquire() {
        // TODO: Add your code here...
        synchronized (this) {
            while (isPut || activeGet >= k) {
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
     * Releases a read lock and notifies waiting threads.
     *
     * @throws IllegalMonitorStateException if there are no active readers.
     */
    public void readRelease() {
        // TODO: Add your code here...
        synchronized (this) {
            if (activeGet <= 0) {
                throw new IllegalMonitorStateException("Illegal read release attempt");
            }
            activeGet--;
            if (activeGet == 0) {
                notifyAll();
            }
        }
    }

    /**
     * Acquires a write lock, blocking until it can be obtained.
     *
     * @throws RuntimeException if the current thread is interrupted while waiting.
     */
    public void writeAcquire() {
        // TODO: Add your code here...
        synchronized (this) {
            while (isPut || activeGet > 0) {
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
     * Attempts to acquire a write lock without blocking.
     * If successful, sets the isWriting flag to true and returns true; otherwise, returns false.
     *
     * @return true if the write lock is acquired, false otherwise.
     */
    public boolean writeTryAcquire() {
        // TODO: Add your code here...
        synchronized (this) {
            if (!isPut && activeGet == 0) {
                isPut = true;
                return true;
            }
            return false;
        }
    }

    /**
     * Releases a write lock, notifies waiting threads, and sets the isWriting flag to false.
     *
     * @throws IllegalMonitorStateException if the current thread is not the one currently writing.
     */
    public void writeRelease() {
        // TODO: Add your code here...
        synchronized (this) {
            if (!isPut) {
                throw new IllegalMonitorStateException("Illegal write release attempt");
            }
            isPut = false;
            notifyAll();
        }
    }
}