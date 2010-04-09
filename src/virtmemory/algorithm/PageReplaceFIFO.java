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

    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        return physMem.getFirst();                      // a legelső
    }


    /*
     * FIXME: nemtudom ezeket implementálni kell-e, a readByte/writeByte miatt?
     * de biztos, hogy nem kell csinálniuk semmit.
     */
    public void doTheAccountingOnRead(Page used, LinkedList<Page> physMem) {

    }

    public void doTheAccountingOnWrite(Page used, LinkedList<Page> physMem) {

    }

    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem) {

    }

}
