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

    /** Egyedüli példány. */
    private static PageReplaceLRU instance = null;

    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        return physMem.getFirst();                      // a legelső
    }

    
    /**
     * Adminisztratív tevékenységek lapról történő olvasáskor.
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnRead(Page used, LinkedList<Page> physMem) {
        physMem.remove(used);
        physMem.addLast(used);
    }

    /**
     * Adminisztratív tevékenységek lapa történő íráskor.
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnWrite(Page used, LinkedList<Page> physMem) {
        physMem.remove(used);
        physMem.addLast(used);
    }

    /**
     * Adminisztratív tevékenységek lapcserénél.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem) {
    }


     /** Üres konstruktor */
    private PageReplaceLRU() {

    }

    /**
     * Singleton
     * @return
     */
    public static PageReplaceLRU getInstance() {
        if (instance == null) {
            instance = new PageReplaceLRU();
        }
        return instance;
    }

}
