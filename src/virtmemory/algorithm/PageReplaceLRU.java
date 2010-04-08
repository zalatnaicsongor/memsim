package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.*;

/**
 * LRU lapcserélő algolritmus.
 *   Láncolt lista: elején a legrégebben, végén a legutóbb használt lap.
 *   Minden memóriahivatkozásnál (!) a hivatkozott lapot a lista végére kell fűzni.
 *   Laphiba esetén a legelső lapot dobjuk ki.
 *
 * @author Kádár István
 */
public class PageReplaceLRU implements PageReplaceStrategy {

    /**
     * A lapon történt eseményeket adminisztrálása. (pl.: usageCount++, lista végére fűzés)
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccounting(Page used, LinkedList<Page> physMem) {
        // a lista végére fűzzük a legelső lapot
        Page first = physMem.getFirst();
        physMem.addLast(first);
        physMem.removeFirst();
    }


    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        return physMem.getFirst();                      // a legelső
    }

}
