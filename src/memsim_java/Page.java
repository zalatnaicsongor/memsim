package memsim_java;

/**
 * Lap osztály.
 * A virtuális címtartomány egysége.
 *
 * @author Kádár István
 */
public class Page {

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
        dirty = false;
        ref = false;
        isInMemory = false;                         // kezdetben egyetlen lap sincs a memóriában
        pageNumber = number;
        data = new int[Memory.PAGESIZE];            // ?
    }

    //byte olvasása a lapról
    public int readByte(int physicalAddress) {
        return data[physicalAddress];
        //itt egy HASZNÁLTAM++ kellene, de a ref az nem az (akkor mi is??)
    }

    // Getterek

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
