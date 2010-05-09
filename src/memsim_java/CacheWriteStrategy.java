package memsim_java;

/**
 * a megváltozott row-k visszaírási stratégiáinak közös interfésze
 * @author zalatnaicsongor
 */
public interface CacheWriteStrategy {
    /**
     * egy byte visszaírása
     * @param address - hova
     * @param data - mit
     */
    public void writeByte(int address, int data);

    /**
     * mi történjen a row kidobásakor
     * @param cr
     */
    public void onDiscard(CacheRow cr);
}
