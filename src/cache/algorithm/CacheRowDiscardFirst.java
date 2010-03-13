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
public class CacheRowDiscardFirst implements CacheRowDiscardStrategy {

    private static CacheRowDiscardFirst instance;

    public CacheRow findRow(CacheLine cLine) {
        System.out.println("--------");
        System.out.println("Kilőttem egy sort");
        System.out.println("--------");
        CacheRow cr = cLine.cacheRowArray[0];
        return cLine.cacheRowArray[0];
    }

    private CacheRowDiscardFirst() {

    }

    public static CacheRowDiscardFirst getInstance() {
        if (CacheRowDiscardFirst.instance == null) {
            CacheRowDiscardFirst.instance = new CacheRowDiscardFirst();
        }
        return CacheRowDiscardFirst.instance;
    }

}
