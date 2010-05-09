package memsim_java;
/**
 * Egy gyorsítósort reprezentáló osztály
 * @author zalatnaicsongor
 */
public class CacheLine {

    private int sequence = 0;
    private int line;
    public CacheRow[] cacheRowArray;

    /**
     * getter
     * @return line
     */
    public int getLine() {
        return line;
    }

    /**
     * minden row-t kidob a line-ban
     */
    public void destroyAll() {
        for (CacheRow cr: cacheRowArray) {
            cr.discard();
        }
    }

    public void resetSequence() {
        this.sequence = 0;
    }

    /**
     * alaphelyzetbe állítja a row-k használatát
     */
    public void resetUsageData() {
        for (CacheRow cr: cacheRowArray) {
            cr.resetUsageData();
        }
    }

    /**
     * lekéri a következő számot a szekvenciából
     * @return
     */
    public int getNextSequence() {
        int retval = this.sequence;
        this.sequence++;
        return retval;
    }

    /**
     * konstruktor
     * beállítja a line-t és a benne lévő row-kat
     * @param line
     * @param associativity
     */
    public CacheLine(int line, int associativity) {
        this.line = line;
        this.cacheRowArray = new CacheRow[associativity];
        //for (int i = 0; i < Cache.getInstance().getAssociativity(); i++) {
        //    this.cacheRowArray.set(i, null);
        //}
    }

    /**
     * Visszaad egy row-t tag alapján
     * @param tag
     * @return row
     * @throws CacheRowNotFoundException
     */
    public CacheRow getRowByTag(int tag) throws CacheRowNotFoundException {
        CacheRow retval = null;
        for (CacheRow cr: this.cacheRowArray) {
            if (cr != null) {
                if (cr.getTag() == tag) {
                    retval = cr;
                    break;
                }
            }
        }
        if (retval == null) {
            throw new CacheRowNotFoundException("Nem találtam meg a sort");
        } else if (!retval.isValid()) {
            throw new CacheRowNotFoundException("A sor nem valid");
        }
        return retval;
    }

    /**
     * egy új row hozzáadása ehhez a line-hoz
     * ha már nincs hely, akkor kidobjuk valamelyiket
     * @param tag
     * @return a row
     */
    public CacheRow createRow(int tag) {
        CacheRow retval = null;
        while (this.getFreeRowsCount() <= 0) {
            Cache.getInstance().getRowDiscardStrategy().findRow(this).discard();
            resetUsageData();
            resetSequence();
        }
        retval = new CacheRow(tag, this);
        this.cacheRowArray[this.findFreeIndex()] = retval;
        return retval;
    }

    /**
     * keres egy szabad rowt ebben a line-ban
     * és visszaadja az indexét
     * @return index
     */
    public int findFreeIndex() {
        int index = -1;
        int i = 0;
        for (CacheRow cr: this.cacheRowArray) {
            if (cr == null || !cr.isValid()) {
                System.out.println(i);
                return i;
            }
            i++;
        }
        return index;
    }

    /**
     * megszámolja, hány üres hely van ebben a line-ban
     * @return count
     */
    public int getFreeRowsCount() {
        int retval = 0;
        for (CacheRow cr: this.cacheRowArray) {
            if (cr == null || !cr.isValid()) {
                retval++;
            }
        }
        return retval;
    }

    /**
     * megkeresi a legkevesebbszer használt row-t
     * @return row
     */
    public CacheRow getMinUsageCountCacheRow() {
        CacheRow retval = this.cacheRowArray[0];
        for (CacheRow cr: this.cacheRowArray) {
            if (cr.getUseCount() < retval.getUseCount()) {
                retval = cr;
            }
        }
        return retval;
    }

    /**
     * megkeresi a legtöbbször használt row-t
     * @return row
     */
    public CacheRow getMaxUsageCountCacheRow() {
        CacheRow retval = this.cacheRowArray[0];
        for (CacheRow cr: this.cacheRowArray) {
            if (cr.getUseCount() > retval.getUseCount()) {
                retval = cr;
            }
        }
        return retval;
    }

    /**
     * megkeresi a legrégebben használt row-t
     * @return row
     */
    public CacheRow getMinUsageSequenceCacheRow() {
        CacheRow retval = this.cacheRowArray[0];
        for (CacheRow cr: this.cacheRowArray) {
            if (cr.getUseSequence() < retval.getUseSequence()) {
                retval = cr;
            }
        }
        return retval;
    }

    /**
     * megkeresi a legutóbb használt rowt
     * @return row
     */
    public CacheRow getMaxUsageSequenceCacheRow() {
        CacheRow retval = this.cacheRowArray[0];
        for (CacheRow cr: this.cacheRowArray) {
            if (cr.getUseSequence() > retval.getUseSequence()) {
                retval = cr;
            }
        }
        return retval;
    }

}
