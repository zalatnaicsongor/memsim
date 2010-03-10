/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memsim_java;
/**
 *
 * @author zalatnaicsongor
 */
public class CacheLine {
    private int line;
    CacheRow[] cacheRowArray;

    public int getLine() {
        return line;
    }

    public CacheLine(int line, int associativity) {
        this.line = line;
        this.cacheRowArray = new CacheRow[associativity];
        //for (int i = 0; i < Cache.getInstance().getAssociativity(); i++) {
        //    this.cacheRowArray.set(i, null);
        //}
    }

    public CacheRow getRowByTag(int tag) throws CacheRowNotFoundException {
        CacheRow retval = null;
        for (CacheRow cr: this.cacheRowArray) {
            if (cr.getTag() == tag) {
                retval = cr;
                break;
            }
        }
        if (retval == null) {
            throw new CacheRowNotFoundException("Nem találtam meg a sort");
        } else if (!retval.isValid()) {
            throw new CacheRowNotFoundException("A sor nem valid");
        }
        return retval;
    }

    public CacheRow createRow(int tag) {
        CacheRow retval = null;
        while (this.getFreeRowsCount() <= 0) {
            Cache.getInstance().getRowDiscardStrategy().findRow(cacheRowArray).discard();
        }
        this.cacheRowArray[this.findFreeIndex()] = new CacheRow(tag, this);
        return retval;
    }
    public int findFreeIndex() {
        int index = -1;
        for (int i = 0; i < Cache.getInstance().getAssociativity(); i++) {
            if (this.cacheRowArray[i] == null || this.cacheRowArray[i].isValid()) {
                return i;
            }
        }
        return index;
    }

    public int getFreeRowsCount() {
        int retval = 0;
        for (CacheRow cr: this.cacheRowArray) {
            if (cr == null || !cr.isValid()) {
                retval++;
            }
        }
        return retval;
    }

}