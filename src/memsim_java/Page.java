package memsim_java;

/**
 * Lap osztály.
 * A virtuális címtartomány egysége.
 *
 * @author Kádár István
 */
public class Page {

    /**
     * A lapméret bájtokban.
     *
     * Néhány lehetőség ha lapmérettel akarunk variálni (16 bites cimtartomány esetén):
     *      16 KB   4 lap
     *      4 KB    16 lap
     *      2 KB    32 lap
     *      512 B   128 lap
     *      256 B   256 lap
     *
     * FIXME
     *   a lapméretet talán a memory osztályban is elhelyezhetnénk
     */
    private int size;

    /**
     * Dirty bit.
     * Ha a lapon modosítás történt, értéke true.
     */
    public boolean dirty;

    /**
     * Hivatkozott bit.
     * Lapra történő hivatkozáskor (olvasás/írás) lesz true.
     */
    public boolean ref;

    /**
     * Jelenlét/hiány
     * Ha true, a lap bennt van a fizikai memóriában.
     */
    public boolean isInMemory;

    /**
     * A lap sorszáma, azonosítója.
     */
    public int pageNumber;


    /**
     * A lapon lévő adat.
     */
    public int[] data;


    /**
     * Konstruktor
     * @param number A lapsorszám.
     */
    public Page(int number) {
        size = 4096;                // 4 KB-os lapméret
        dirty = false;
        ref = false;
        isInMemory = false;         // kezdetben egyetlen lap sincsa a memóriában
        pageNumber = number;
    }


    // Getterk

    public boolean getDirty() {
        return dirty;
    }

   public boolean getRef() {
        return ref;
    }

    public boolean getIsInMemory() {
        return isInMemory;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int[] getData() {
        return data;
    }


    // Setterek

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setRef(boolean ref) {
        this.ref = ref;
    }

    public void setIsInMemory(boolean isInMemory) {
        this.isInMemory = isInMemory;
    }

    public void setData(int[] data) {
        this.data = data;
    }


}
