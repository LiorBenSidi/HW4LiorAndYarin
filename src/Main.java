public class Main {
    public static void main(String[] args) {
        testPartA();
        testPartB();
    }

    private static void testPartA() {
        System.out.println("Testing part A...");
        testPartA1();
        testPartA2();
    }

    private static void testPartA1() {
        System.out.println("Testing part A1...");
        BinNode<Character> root = new BinNode<>('h');
        root.setLeft(new BinNode<>('e'));
        root.setRight(new BinNode<>('a'));
        root.getLeft().setLeft(new BinNode<>('B', new BinNode<>('k'), null));
        root.getLeft().setRight(new BinNode<>('y', new BinNode<>('M'), new BinNode<>('!')));

        String[] inputs = {"hey", "Hey", "h", "k", "aaa", "hE", "hA", "ha", "@123", "he", "hex"};

        for (String input : inputs) {
            boolean result = PathFromRoot.doesPathExist(root, input);
            System.out.println("Path \"" + input + "\"? " + result);
        }

        System.out.println();
    }

    private static void testPartA2() {
        System.out.println("Testing part A2...");
        BinNode<Integer> root = new BinNode<>(5);
        root.setLeft(new BinNode<>(7));
        root.setRight(new BinNode<>(3));
        root.getLeft().setLeft(new BinNode<>(7));
        root.getLeft().setRight(new BinNode<>(9));
        root.getLeft().getLeft().setLeft(new BinNode<>(5));
        root.getLeft().getRight().setLeft(new BinNode<>(9));
        root.getLeft().getRight().setRight(new BinNode<>(1));
        testPartA2(root);
    }

    private static void testPartA2(BinNode<Integer> root) {
        int level = LevelLargestSum.getLevelWithLargestSum(root);
        System.out.println("Level with largest sum: " + level);
        if (root != null) {
            testPartA2(root.getLeft());
            testPartA2(root.getRight());
        }
    }

    private static void testPartB() {
        System.out.println("Testing part B...");
        int numOfReaders = 10;  // Note: You cannot assume this is the only value!
        testExceptions(numOfReaders);
        for (int m = 1; m <= 5; m++) {  // Test 5 times, since the output is non-deterministic
            System.out.println("+++++++++++++++++++++++++++++++++ Starts round " + m + " +++++++++++++++++++++++++++++++++");
            Database db = new Database(numOfReaders);
            performThreeTests(db, numOfReaders, false);
            performThreeTests(db, numOfReaders, true);
        }
    }

    private static void testExceptions(int numReaders) {
        Database db = new Database(numReaders);
        try {
            db.readRelease();
        } catch (IllegalMonitorStateException e) {
            System.out.println(e.getMessage());
        }
        try {
            db.writeRelease();
        } catch (IllegalMonitorStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void performThreeTests(Database db, int numOfReaders, boolean useTry) {
        testRead(db, numOfReaders, useTry);
        System.out.println("----------------------------------------------------------------------------------");
        testWrite(db, useTry);
        System.out.println("----------------------------------------------------------------------------------");
        testReadWrite(db, useTry);
        if (!useTry) {
            System.out.println("----------------------------------------------------------------------------------");
        }
    }

    private static void testRead(Database db, int numOfReaders, boolean useTry) {
        if (useTry) {
            System.out.println("Testing reading using try...");
        } else {
            System.out.println("Testing reading...");
        }
        Reader[] readers = new Reader[numOfReaders + 1];

        for (int i = 0; i < readers.length; i++) {
            readers[i] = new Reader(db, i + 1, useTry);
        }
        for (Reader reader : readers) {
            reader.start();
        }
        for (Reader reader : readers) {
            try {
                reader.join();
            } catch (InterruptedException e) {

            }
        }
    }

    private static void testWrite(Database db, boolean useTry) {
        if (useTry) {
            System.out.println("Testing writing using try...");
        } else {
            System.out.println("Testing writing...");
        }
        Writer writer1 = new Writer(db, 1, useTry);
        Writer writer2 = new Writer(db, 2, useTry);
        writer1.start();
        writer2.start();
        try {
            writer1.join();
            writer2.join();
        } catch (InterruptedException e) {

        }
    }

    private static void testReadWrite(Database db, boolean useTry) {
        if (useTry) {
            System.out.println("Testing reading and writing using try...");
        } else {
            System.out.println("Testing reading and writing...");
        }

        Writer writer = new Writer(db, 1, "This is", "a test");
        Reader reader = new Reader(db, 2, useTry, "This is");
        writer.start();
        Worker.sleep(50);  // Make sure the writer starts first
        reader.start();
        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
        }
    }
}

abstract class Worker extends Thread {
    protected Database db;
    protected int threadNum;
    protected boolean useTry;

    public Worker(Database db, int threadNum, boolean useTry) {
        this.db = db;
        this.threadNum = threadNum;
        this.useTry = useTry;
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public abstract void run();
}


class Reader extends Worker {
    private String keyToRetrieve;

    public Reader(Database db, int threadNum, boolean useTry, String keyToRetrieve) {
        super(db, threadNum, useTry);
        this.keyToRetrieve = keyToRetrieve;
    }

    public Reader(Database db, int threadNum, String keyToRetrieve) {
        this(db, threadNum, false, keyToRetrieve);
    }

    public Reader(Database db, int threadNum, boolean useTry) {
        this(db, threadNum, useTry, null);
    }

    @Override
    public void run() {
        if (keyToRetrieve == null) {
            System.out.println(threadNum + " Starting as Reader...");
        }
        if (!useTry) {
            db.readAcquire();
        } else {
            boolean success = db.readTryAcquire();
            if (!success) {
                System.out.println(threadNum + " Is unable to read!");
                return;
            }
        }
        try {
            System.out.println(threadNum + " Is able to read!");
            if (keyToRetrieve != null) {
                String value = db.get(keyToRetrieve);
                System.out.println(threadNum + " Retrieved " + keyToRetrieve + ": " + value);
            }
            sleep(100);
            System.out.println(threadNum + " Done reading.");
        } finally {
            db.readRelease();
        }
    }
}

class Writer extends Worker {
    private String keyToWrite;
    private String valueToWrite;

    public Writer(Database db, int threadNum, boolean useTry, String keyToWrite, String valueToWrite) {
        super(db, threadNum, useTry);
        this.keyToWrite = keyToWrite;
        this.valueToWrite = valueToWrite;
    }

    public Writer(Database db, int threadNum, String keyToWrite, String valueToWrite) {
        this(db, threadNum, false, keyToWrite, valueToWrite);
    }

    public Writer(Database db, int threadNum) {
        this(db, threadNum, null, null);
    }

    public Writer(Database db, int threadNum, boolean useTry) {
        this(db, threadNum, useTry, null, null);
    }

    @Override
    public void run() {
        System.out.println(threadNum + " Starting as Writer...");
        if (!useTry) {
            db.writeAcquire();
        } else {
            boolean success = db.writeTryAcquire();
            if (!success) {
                System.out.println(threadNum + " Is unable to write!");
                return;
            }
        }
        try {
            System.out.println(threadNum + " Is able to write!");
            sleep(100);
            if (keyToWrite != null) {
                db.put(keyToWrite, valueToWrite);
            }
            System.out.println(threadNum + " Done writing.");
        } finally {
            db.writeRelease();
        }
    }
}
