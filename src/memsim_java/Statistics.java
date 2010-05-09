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

    /**
     * A Statistics-példány lekérése
     * @return instance
     */
    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    /**
     * üres konstruktor, a singleton miatt private
     */
    private Statistics() {

    }

    /**
     * Egy cache-fault hozzáadása
     */
    public void addCacheFault() {
        this.cacheFault++;
        System.out.println("CacheFAULT");
    }

    /**
     * Egy cache-használat hozzáadása
     */
    public void useCache() {
        this.cacheUsed++;
        System.out.println("CacheUSE");
    }

    /**
     * Egy memóriahasználat elkönyvelése
     * @param type
     */
    public void useMemory(String type) {
        if (type.equals("read")) {
            System.out.println("MemoryREAD");
            this.memoryUsedRead++;
        } else {
            this.memoryUsedWrite++;
            System.out.println("MemoryWRITE");
        }
    }

    /**
     * Laphiány eltárolása
     */
    public void addPageFault() {
        this.pageFault++;
        System.out.println("PageFAULT");
    }

    /**
     * Kompaktálás eltárolása
     */
    public void addCompact() {
        this.numberOfCompacts++;
        System.out.println("MemoryCOMPACTED");
    }

    /**
     * Olvasás virtuális memóriából
     */
    public void useVirtualRead() {
        //lap ki-be == lap mérete * elérés ideje (elérési idő byteonként van számolva!!!)
        this.virtualUsedRead += Memory.getInstance().PAGESIZE;
        // nem csak úgy bevándorol a memóriába, hanem bele is kell írni, ami IDŐ
        this.memoryUsedWrite += Memory.getInstance().PAGESIZE;
        System.out.println("VirtualREAD");
    }

    /**
     * Írás virtuális memóriába
     */
    public void useVirtualWrite() {
        //lap ki-be == lap mérete * elérés ideje (elérési idő byteonként van számolva!!!)
        this.virtualUsedWrite += Memory.getInstance().PAGESIZE;
        // a memóriából ki fogja kiolvasni a tárolóra írandó változásokat?
        this.memoryUsedRead += Memory.getInstance().PAGESIZE;
        System.out.println("VirtualWRITE");
    }

    /**
     * Az összegzett adatok exportálása csv formában
     * @param fileName
     */
    public void exportCSV(String fileName) {
        int a = (Statistics.memoryReadTime * this.memoryUsedRead + Statistics.memoryWriteTime * this.memoryUsedWrite);
        int b = (this.virtualUsedRead + this.virtualUsedWrite) * Statistics.virtualTime;
        double c = 0.0;
        if (this.cacheUsed != 0) {
            c = 1 - (this.cacheFault / this.cacheUsed);
        }

        String answer = "CacheUse,CacheTime,MemUseRead,MemUseWrite,MemTime," +
                "VirtUseRead,VirtUseWrite,VirtTime,CacheFault,PageFault,CacheFoundRatio,Compacts\n" +
                this.cacheUsed + "," + this.cacheUsed*Statistics.cacheTime + "," +
                this.memoryUsedRead + "," + this.memoryUsedWrite + "," + a + "," +
                this.virtualUsedRead + "," + this.virtualUsedWrite + "," + b + "," +
                this.cacheFault + "," + this.pageFault + "," + c + "," + this.numberOfCompacts + "\n";

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(answer);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
