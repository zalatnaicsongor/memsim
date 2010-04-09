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

    /** Egyedüli példány. */
    private static PageReplaceRandom instance = null;

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

    public void doTheAccountingOnRead(Page used, LinkedList<Page> physMem) {
        
    }

    public void doTheAccountingOnWrite(Page used, LinkedList<Page> physMem) {

    }

    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem) {

    }


    /** Üres konstruktor */
    private PageReplaceRandom() {

    }

    /**
     * Singleton
     * @return példány
     */
    public static PageReplaceRandom getInstance() {
        if (instance == null) {
            instance = new PageReplaceRandom();
        }
        return instance;
    }
}
