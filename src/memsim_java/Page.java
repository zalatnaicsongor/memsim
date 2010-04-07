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
     * A lap sorszáma.
     */
    public int pageNumber;


    /**
     * A lapon lévő adat.
     */
    public int[] data;


}
