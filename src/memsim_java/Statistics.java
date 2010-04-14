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
    private int virtualUsedRead = 0;
    private int virtualUsedWrite = 0;

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

    public void useVirtualRead() {
        //lap ki-be == lap mérete * elérés ideje (elérési idő byteonként van számolva!!!)
        this.virtualUsedRead += Memory.PAGESIZE;
        // nem csak úgy bevándorol a memóriába, hanem bele is kell írni, ami IDŐ
        this.memoryUsedWrite += Memory.PAGESIZE;
        System.out.println("VirtualREAD");
    }
    public void useVirtualWrite() {
        //lap ki-be == lap mérete * elérés ideje (elérési idő byteonként van számolva!!!)
        this.virtualUsedWrite += Memory.PAGESIZE;
        // a memóriából ki fogja kiolvasni a tárolóra írandó változásokat?
        this.memoryUsedRead += Memory.PAGESIZE;
        System.out.println("VirtualWRITE");
    }

    public void exportCSV(String fileName) {
        int a = (Statistics.memoryReadTime * this.memoryUsedRead + Statistics.memoryWriteTime * this.memoryUsedWrite);
        double b = 0.0;
        if (this.cacheUsed != 0) {
            b = 1 - (this.cacheFault / this.cacheUsed);
        }
        double c = 1 - (this.pageFault / (this.memoryUsedRead + this.memoryUsedWrite));
        String answer =  "CacheUse," + this.cacheUsed + "\n" +
              "CacheTime" + this.cacheUsed * Statistics.cacheTime + "\n" +
              "MemUseRead," + this.memoryUsedRead + "\n" +
              "MemUseWrite," + this.memoryUsedWrite + "\n" +
              "MemTime," + a + "\n" +
              "VirtUseRead," + this.virtualUsedRead + "\n" +
              "VirtUseWrite," + this.virtualUsedWrite + "\n" +
              "VirtTime," + (this.virtualUsedRead + this.virtualUsedWrite) * Statistics.virtualTime + "\n" +
              "CacheFault," + this.cacheFault + "\n" +
              "PageFault," + this.pageFault + "\n" +
              "CacheFoundRatio," + b + "\n" +
              //"MemFoundRatio," + c + "\n" +
              "Compacts," + this.numberOfCompacts + "\n";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(answer);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
