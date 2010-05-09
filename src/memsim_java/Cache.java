package memsim_java;
import cache.algorithm.*;
/**
 * A cache-t reprezentáló osztály
 * Állítható paraméterek:
 * - sorméret
 * - sorok száma
 * - asszociativitás mértéke
 * - soreldobó és visszaíró stratégia
 * @author zalatnaicsongor
 */
public class Cache {
    private int rowSize;
    private int numRows;
    private int associativity;
    private static Cache instance;

    private int lineLength;
    private int tagLength;
    private int displacementLength;

    private CacheWriteStrategy writeStrategy = CacheWriteAllocator.getInstance();
    private CacheRowDiscardStrategy rowDiscardStrategy = CacheRowDiscardFirst.getInstance();

    private CacheLine[] lines;

    /**
     * Minden gyorsítósor kidobása
     */
    public void destroyAll() {
        for (CacheLine cr: lines) {
            cr.destroyAll();
        }
    }

    /**
     * Gyorsítósor kidobása cím alapján
     * @param address
     */
    public void destroyByAddress(int address) {
        int line = this.genLine(address);
        int tag = this.genTag(address);
        try {
            CacheLine cLine = this.getLine(line);
            CacheRow cRow = cLine.getRowByTag(tag);
            cRow.discard();
        } catch (CacheRowNotFoundException e) {
            System.out.println("Nem is volt bent a cache-ben, nem kell kidobni");
        }
    }

    /**
     * Visszaadja a "line" sorszámú line-t
     * @param line
     * @return CacheLine
     */
    public CacheLine getLine(int line) {
        return lines[line];
    }

    /**
     * Visszaadja a jelenleg használt kidobó-stratégiát
     * @return CacheRowDiscardStrategy
     */
    public CacheRowDiscardStrategy getRowDiscardStrategy() {
        return rowDiscardStrategy;
    }

    /**
     * Beállítja a sorkidobó stratégiát
     * @param rowDiscardStrategy
     */
    public void setRowDiscardStrategy(CacheRowDiscardStrategy rowDiscardStrategy) {
        this.rowDiscardStrategy = rowDiscardStrategy;
    }

    /**
     * Visszaadja a jelenleg használt író-stratégiát
     * @return CacheRowDiscardStrategy
     */
    public CacheWriteStrategy getWriteStrategy() {
        return writeStrategy;
    }

    /**
     * Beállítja az íróstratégiát
     * @param rowDiscardStrategy
     */
    public void setWriteStrategy(CacheWriteStrategy writeStrategy) {
        this.writeStrategy = writeStrategy;
    }

    /**
     * Lekérdezi az asszociatibitást
     * @return int
     */
    public int getAssociativity() {
        return associativity;
    }

    /**
     * Lekérdezi a sorok számát
     * @return int
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Lekérdezi a sorméretet
     * @return int
     */
    public int getRowSize() {
        return rowSize;
    }

    /**
     * Lekérdezi a teljes cache-méretet
     * @return int
     */
    public int getSize() {
        return numRows * rowSize * associativity;
    }

    /**
     * Line generálása cím alapján
     * @param address
     * @return line
     */
    public int genLine(int address) {
        int remainLength = Memory.getInstance().ADDRESSLENGTH - this.tagLength - this.lineLength;
        int mask = (int)(Math.pow(2, this.lineLength) - 1) << remainLength;
        return ((mask & address) >>> remainLength);
    }

    /**
     * Tag generálása cím alapján
     * @param address
     * @return tag
     */
    public int genTag(int address) {
        int remainLength = Memory.getInstance().ADDRESSLENGTH - this.tagLength;
        int mask = (int)(Math.pow(2, this.tagLength) - 1) << remainLength;
        return ((mask & address) >>> remainLength);
    }

    /**
     * Eltolás generálása cím alapján
     * @param address
     * @return displacement
     */
    public int genDisplacement(int address) {
        int mask = (int)(Math.pow(2, this.displacementLength) - 1);
        return (mask & address);
    }

    /**
     * Cím visszagenerálása tag-ből, line-ból és eltolásból
     * @param tag
     * @param line
     * @param displacement
     * @return address
     */
    public int genAddress(int tag, int line, int displacement) {
        int retval = 0;
        retval = retval | displacement;
        retval |= line << this.displacementLength;
        retval |= tag << (this.displacementLength + this.lineLength);
        return retval;
    }

    /**
     * Egy byte olvasása cache-ből
     * @param address
     * @return byte
     */
    public int readByte(int address) {
        int line = Cache.getInstance().genLine(address);
        int tag = Cache.getInstance().genTag(address);
        int displacement = Cache.getInstance().genDisplacement(address);

        CacheRow row = null;

        try {
            row = Cache.getInstance().getLine(line).getRowByTag(tag);
        } catch (CacheRowNotFoundException e) {
            System.out.println(e);
            Statistics.getInstance().addCacheFault(); //inkrementáljuk a cachefault változót
            row = Cache.getInstance().getLine(line).createRow(tag);
        }
        Statistics.getInstance().useCache();
        return row.readByte(displacement);
    }

    /**
     * Egy byte írása cache-be
     * @param address - hova
     * @param data - mit
     */
    public void writeByte(int address, int data) {
        this.getWriteStrategy().writeByte(address, data);
    }

    /**
     * Ellenőrzéshez, hogy tényleg 2 hatványa-e az, aminek kell
     * @param a - a szám
     * @return log2(a)
     */
    public static int logKetto(int a) {
        int r = 0;
        while (true) {
            a = a >>> 1;
            if (a<=0) {
                break;
            }
            r++;
        }
        return r;
    }

    /**
     * Protected konstruktor, paraméterek a változtatható értékek
     * @param rowSize
     * @param numRows
     * @param associativity
     * @throws Exception
     */
    protected Cache(int rowSize, int numRows, int associativity) throws Exception {
        if ((rowSize & (rowSize - 1)) != 0 || rowSize <= 0) {
            throw new Exception("rowSize nem kettohatvany, vagy 0, vagy negativ");
        }
        if ((numRows & (numRows - 1)) != 0 || numRows <= 0) {
            throw new Exception("numRows nem kettohatvany, vagy 0, vagy negativ");
        }
        if ((associativity & (associativity - 1)) != 0 || associativity <= 0) {
            throw new Exception("associativity nem kettohatvany, vagy 0, vagy negativ");
        }
        this.rowSize = rowSize;
        this.numRows = numRows;
        this.associativity = associativity;
        this.lineLength = Cache.logKetto(numRows);
        this.displacementLength = Cache.logKetto(rowSize);
        this.tagLength = Memory.getInstance().ADDRESSLENGTH - this.lineLength - this.displacementLength;
        if (this.tagLength <= 0) {
            throw new Exception("itt valami rossz történt");
        }
        this.lines = new CacheLine[numRows];
        for (int i = 0; i < numRows; i++) {
            this.lines[i] = new CacheLine(i, associativity);
        }
    }

    /**
     * Cache elkészítése
     * @param rowSize
     * @param numRows
     * @param associativity
     * @throws Exception
     */
    public static void create(int rowSize, int numRows, int associativity) throws Exception {
        try {
            Cache.instance = new Cache(rowSize, numRows, associativity);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Cache-példány lekérése
     * @return instance
     */
    public static Cache getInstance() {
        return Cache.instance;
    }

}
