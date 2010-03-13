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
public class CacheRowDiscardLRU implements CacheRowDiscardStrategy {

    private static CacheRowDiscardLRU instance;

    public CacheRow findRow(CacheLine cLine) {
        System.out.println("--------");
        System.out.println("Kilőttem egy sort");
        System.out.println("--------");
        CacheRow cr = cLine.getMinUsageSequenceCacheRow();
        return cr;
    }

    private CacheRowDiscardLRU() {

    }

    public static CacheRowDiscardLRU getInstance() {
        if (CacheRowDiscardLRU.instance == null) {
            CacheRowDiscardLRU.instance = new CacheRowDiscardLRU();
        }
        return CacheRowDiscardLRU.instance;
    }

}
