/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memsim_java;
/**
 * egy cache-row-t ábrázoló class
 * @author zalatnaicsongor
 */
public class CacheRow {
    private int tag;
    private CacheLine container;
    private boolean valid = true;
    private boolean dirty = false;
    private int useCount = 0;
    private int useSequence = 0;

    private int[] data;

    /**
     * egy byte olvasása displacement eltolással
     * @param displacement
     * @return byte
     */
    public int readByte(int displacement) {
        updateUsageData();
        return data[displacement];
    }

    /**
     * egy byte írása displacement eltolással
     * @param displacement
     * @param data
     */
    public void writeByte(int displacement, int data) {
        this.setDirty(true);
        updateUsageData();
        Statistics.getInstance().useCache();
        this.data[displacement] = data;
    }

    /**
     * adminisztrálja a row használatát
     */
    private void updateUsageData() {
        this.useSequence = container.getNextSequence();
        this.useCount++;
    }

    /**
     * alaphelyzetbe állítja a row használati adatait
     */
    public void resetUsageData() {
        this.useCount = 0;
        this.useSequence = 0;
    }

    /**
     * lekéri a tag-et
     * @return tag
     */
    public int getTag() {
        return tag;
    }

    /**
     * hányszor használtuk ezt a row-t
     * @return count
     */
    public int getUseCount() {
        return useCount;
    }

    /**
     * usagecount beállítása
     * @param useCount
     */
    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    /**
     * lekéri a jelenlegi használati szevenciáját
     * @return seq
     */
    public int getUseSequence() {
        return useSequence;
    }

    /**
     * frissíti a használati szekvenciát
     * @param useSequence
     */
    public void setUseSequence(int useSequence) {
        this.useSequence = useSequence;
    }

    /**
     * megadja, hogy a row-ban lévő adat valid-e
     * @return boolean
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * beállítja a row valid-bitjét
     * @param valid
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * beleírtunk-e már a rowba
     * @return boolean
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * a row "bepiszkolása"
     * @param dirty
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * row érvénytelenítése line-ban
     * (és szükség esetén visszaírása)
     */
    public void discard() {
        Cache.getInstance().getWriteStrategy().onDiscard(this);
        this.setValid(false);
    }

    /**
     * a row megváltozott tartalmát írja vissza a memóriába
     */
    public void writeBack() {
        int startAddress = 0;
        startAddress = Cache.getInstance().genAddress(tag, container.getLine(), 0);
        for (int i = 0; i < Cache.getInstance().getRowSize(); i++) {
            Memory.getInstance().writeByte(startAddress + i, this.readByte(i));
        }
    }

    /**
     * konstruktor
     * itt történik a row feltöltése a memóriából
     * @param tag
     * @param container
     */
    public CacheRow(int tag, CacheLine container) {
        int startAddress = 0;
        this.tag = tag;
        this.container = container;
        this.data = new int[Cache.getInstance().getRowSize()];

        startAddress = Cache.getInstance().genAddress(tag, container.getLine(), 0);
        for (int i = 0; i < Cache.getInstance().getRowSize(); i++) {
            this.data[i] = Memory.getInstance().readByte(startAddress + i);
        }
        System.out.println("Behoztam egy sort, tag: " + tag + " line: " + container.getLine());
        System.out.println("----------");
    }

}
