package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.Page;
import memsim_java.PageReplaceStrategy;

/**
 * FIFO lapcserélő algolritmus.
 *   A lapok egy listában vannak. A lista elején a legkorábban, végén a legutóbb betett lap.
 *   Laphibánál a lista elején lévőt dobjuk el, és végéhez fűzzük az újat.
 *
 * @author Kádár István
 */
public class PageReplaceFIFO implements PageReplaceStrategy {

    /**
     * A lapon történt eseményeket adminisztrálása. (pl.: usageCount++, lista végére fűzés)
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccounting(Page used, LinkedList<Page> physMem) {

    }

    
    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        return physMem.getFirst();
    }

}
