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
    private boolean dirty;

    /**
     * Jelenlét/hiány
     * Ha true, a lap bennt van a fizikai memóriában.
     */
    private boolean isInMemory;

    /**
     * A lap sorszáma, azonosítója.
     */
    private int pageNumber;

    /**
     * A lapon lévő adat.
     */
    public int[] data;


    // lapcsere-algoritmusokhoz tartozó adatok

    /**
     * Hivatkozott bit.
     * Lapra történő hivatkozáskor (olvasás/írás) lesz true.
     */
    private boolean ref;

    /**
     * A lapohoz tartozó számláló.
     * 
     * NFU, Aging algoritmusoknál használatos. Fontos, hogy nem azt tárolja,
     * hogy hányszor történt hivatkozás a lapra, hanem mindig Ref-et adjuk
     * hozzá lapcserekor.
     */
    private long counter;

    /**
     * Konstruktor
     * @param number A lapsorszám.
     */
    public Page(int number) {
        dirty = false;
        isInMemory = false;                         // kezdetben egyetlen lap sincs a memóriában
        pageNumber = number;
        data = new int[Memory.PAGESIZE];            // lapméretű adatterület
        ref = false;
        counter = 0;
    }

    /**
     * Bájt olvasása a lapról.
     * @param physicalAddress Erről a címről.
     * @return A kiolvasott adat.
     */
    public int readByte(int physicalAddress) {
        /*
         * a ref-et nem itt állítjuk be, hanem az algoritmusok csinálják
         */
        return data[physicalAddress];
    }

    /**
     * Bájt írása a lapra.
     * @param address Erre a címre.
     * @param data    Ezt az adatot.
     */
    public void writeByte(int address, int data) {
        /*
         * a ref-et nem itt állítjuk be, hanem az algoritmusok csinálják
         */
        dirty = true;                               // módosítás történt
        this.data[address] = data;
    }

    
    /**
     * Két lap akkor ugyanaz, ha a sorszámuk (pageNumber) megegyezik.
     * @param page A hasonlító lap.
     * @return true ha megegyeznek.
     */
    public boolean equals(Page page) {
        return pageNumber == page.getPageNumber();
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

    public long getCounter() {
        return counter;
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

    public void setCounter(long counter) {
        this.counter = counter;
    }

    /**
     * Eggyel növeli a számláló értékét.
     */
    public void incCounter() {
       this.counter++;
    }

}
