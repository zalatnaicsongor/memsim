package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.*;

/**
 * Random lapcserélő algoritmus.
 *   Véletlenszerűen választja ki a kidobandó lapot.
 *
 * @author Kádár István
 */
public class PageReplaceRandom implements PageReplaceStrategy {

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
        // véletlen szám 0-tól physMem.size() - 1 -ig.
        int rand = (int)(Math.random() * physMem.size());
        return physMem.get(rand);
    }

}
