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
public class CacheRowDiscardLFU implements CacheRowDiscardStrategy {

    private static CacheRowDiscardLFU instance;

    public CacheRow findRow(CacheLine cLine) {
        System.out.println("--------");
        System.out.println("Kilőttem egy sort");
        System.out.println("--------");
        CacheRow cr = cLine.getMinUsageCountCacheRow();
        return cr;
    }

    private CacheRowDiscardLFU() {

    }

    public static CacheRowDiscardLFU getInstance() {
        if (CacheRowDiscardLFU.instance == null) {
            CacheRowDiscardLFU.instance = new CacheRowDiscardLFU();
        }
        return CacheRowDiscardLFU.instance;
    }

}
