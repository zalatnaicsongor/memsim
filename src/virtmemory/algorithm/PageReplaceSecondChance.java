package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.*;

/**
 * SecondChance lapcserélő algolritmus.
 *   A lapok láncolt listába fűzve, mint a FIFO-nál.
 *   Ha az első (legkorábban betett) lap R bitje 0, kidobjuk.
 *   Ha az R bit 1, a lapot a lista végére tesszük és töröljük az R bitjét.
 *
 * @author Kádár István
 */
public class PageReplaceSecondChance implements PageReplaceStrategy {

    /**
     * A lapon történt eseményeket adminisztrálása. (pl.: usageCount++,
     * lista végére fűzés, stb.)
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
        Page first = physMem.getFirst();                // legelső lap a listában

        while (first.getRef()) {                        // amig Ref true
            first.setRef(false);                        // törljük a Ref-et
            physMem.addLast(first);                     // a lista végére fűzzük
            physMem.removeFirst();
            first = physMem.getFirst();
        }

        return first;
    }

}
