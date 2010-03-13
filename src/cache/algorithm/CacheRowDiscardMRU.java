/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cache.algorithm;
import memsim_java.*;
/**
 *
 * @author zalatnaicsongor
 */
public class CacheRowDiscardMRU implements CacheRowDiscardStrategy {

    private static CacheRowDiscardMRU instance;

    public CacheRow findRow(CacheLine cLine) {
        System.out.println("--------");
        System.out.println("Kilőttem egy sort");
        System.out.println("--------");
        CacheRow cr = cLine.getMaxUsageSequenceCacheRow();
        return cr;
    }

    private CacheRowDiscardMRU() {

    }

    public static CacheRowDiscardMRU getInstance() {
        if (CacheRowDiscardMRU.instance == null) {
            CacheRowDiscardMRU.instance = new CacheRowDiscardMRU();
        }
        return CacheRowDiscardMRU.instance;
    }

}
