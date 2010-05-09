/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cache.algorithm;
import memsim_java.*;
/**
 * line megtelésekor az első row-t dobja ki
 * @author zalatnaicsongor
 */
public class CacheRowDiscardFirst implements CacheRowDiscardStrategy {

    private static CacheRowDiscardFirst instance;

    public CacheRow findRow(CacheLine cLine) {
        System.out.println("--------");
        System.out.println("Kilőttem egy sort");
        System.out.println("--------");
        return cLine.cacheRowArray[0];
    }

    /**
     * private konstruktor a singleton miatt
     */
    private CacheRowDiscardFirst() {

    }

    /**
     * az egyetlen instance
     * @return instance
     */
    public static CacheRowDiscardFirst getInstance() {
        if (CacheRowDiscardFirst.instance == null) {
            CacheRowDiscardFirst.instance = new CacheRowDiscardFirst();
        }
        return CacheRowDiscardFirst.instance;
    }

}
