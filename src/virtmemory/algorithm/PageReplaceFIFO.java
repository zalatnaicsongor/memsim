package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.*;

/**
 * FIFO lapcserélő algolritmus.
 *   A lapok egy listában vannak. A lista elején a legkorábban, végén a legutóbb betett lap.
 *   Laphibánál a lista elején lévőt dobjuk el, és végéhez fűzzük az újat.
 *
 * @author Kádár István
 */
public class PageReplaceFIFO implements PageReplaceStrategy {

    /** Egyedüli példány. */
    private static PageReplaceFIFO instance = null;

    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        return physMem.getFirst();                      // a legelső
    }


    public void doTheAccountingOnRead(Page used, LinkedList<Page> physMem) {

    }

    public void doTheAccountingOnWrite(Page used, LinkedList<Page> physMem) {

    }

    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem) {

    }


    /** Üres konstruktor */
    private PageReplaceFIFO() {

    }

    /**
     * Singleton
     * @return
     */
    public static PageReplaceFIFO getInstance() {
        if (instance == null) {
            instance = new PageReplaceFIFO();
        }
        return instance;
    }

}
