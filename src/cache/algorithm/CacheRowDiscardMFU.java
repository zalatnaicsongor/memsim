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
public class CacheRowDiscardMFU implements CacheRowDiscardStrategy {

    private static CacheRowDiscardMFU instance;

    public CacheRow findRow(CacheLine cLine) {
        System.out.println("--------");
        System.out.println("Kilőttem egy sort");
        System.out.println("--------");
        CacheRow cr = cLine.getMaxUsageCountCacheRow();
        return cr;
    }

    private CacheRowDiscardMFU() {

    }

    public static CacheRowDiscardMFU getInstance() {
        if (CacheRowDiscardMFU.instance == null) {
            CacheRowDiscardMFU.instance = new CacheRowDiscardMFU();
        }
        return CacheRowDiscardMFU.instance;
    }

}
