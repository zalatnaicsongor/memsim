/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memsim_java;
/**
 *
 * @author zalatnaicsongor
 */
public interface CacheRowDiscardStrategy {
    /**
     * Megkeresi azt a gyorsítósort, amit el kell dobni.
     * @param cacheRows
     * @return
     */
    public CacheRow findRow(CacheLine cLine);
}
