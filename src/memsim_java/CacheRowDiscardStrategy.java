package memsim_java;
/**
 * a row-kidobó algoritmusok közös interfésze
 * @author zalatnaicsongor
 */
public interface CacheRowDiscardStrategy {
    /**
     * Megkeresi azt a gyorsítósort, amit el kell dobni.
     * @param cacheRows
     * @return row
     */
    public CacheRow findRow(CacheLine cLine);
}
