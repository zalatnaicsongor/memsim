package memsim_java;

import java.io.*;

/**
 * A statisztikát elkészítő osztály
 * @author zealot
 */
public class Statistics {

    //ennyi ideig tart hozzájuk fordulni
    public static final int cacheTime = 1;
    public static final int memoryReadTime = 10;
    public static final int memoryWriteTime = 10;
    public static final int virtualTime = 100;

    //ennyiszer fordultunk hozzájuk ebben a futásban
    private int cacheUsed = 0;
    private int memoryUsedRead = 0;
    private int memoryUsedWrite = 0;
    private int virtualUsed = 0;

    //ennyiszer történt hiba
    private int cacheFault = 0;
    private int pageFault = 0;

    //a töredezettséggel kapcsolatos számok
    private int numberOfCompacts = 0;

    private static Statistics instance = null;

    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    private Statistics() {

    }

    public void addCacheFault() {
        this.cacheFault++;
        System.out.println("CacheFAULT");
    }

    public void useCache() {
        this.cacheUsed++;
        System.out.println("CacheUSE");
    }

    public void useMemory(String type) {
        if (type.equals("read")) {
            System.out.println("MemoryREAD");
            this.memoryUsedRead++;
        } else {
            this.memoryUsedWrite++;
            System.out.println("MemoryWRITE");
        }
    }

    public void addPageFault() {
        this.pageFault++;
        System.out.println("PageFAULT");
    }

    public void addCompact() {
        this.numberOfCompacts++;
        System.out.println("MemoryCOMPACTED");
    }

    public void useVirtual() {
        this.virtualUsed++;
        System.out.println("VirtualUSE");
    }

    public void exportCSV() {
        int a = (Statistics.memoryReadTime * this.memoryUsedRead + Statistics.memoryWriteTime * this.memoryUsedWrite);
        double b = 1 - (this.cacheFault / this.cacheUsed);
        double c = 1 - (this.pageFault / (this.memoryUsedRead + this.memoryUsedWrite));
        String answer =  "CacheUse," + this.cacheUsed + "\n" +
              "CacheTime" + this.cacheUsed * Statistics.cacheTime + "\n" +
              "MemUseRead," + this.memoryUsedRead + "\n" +
              "MemUseWrite," + this.memoryUsedWrite + "\n" +
              "MemTime," + a + "\n" +
              "VirtUse," + this.virtualUsed + "\n" +
              "VirtTime," + this.virtualUsed * Statistics.virtualTime + "\n" +
              "CacheFault," + this.cacheFault + "\n" +
              "PageFault," + this.pageFault + "\n" +
              "CacheFoundRatio," + b + "\n" +
              "MemFoundRatio," + c + "\n" +
              "Compacts," + this.numberOfCompacts + "\n";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("export.csv"));
            out.write(answer);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
