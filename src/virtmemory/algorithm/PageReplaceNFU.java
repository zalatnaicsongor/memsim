package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.*;

/**
 * NFU lapcserélő algolritmus.
 *   Minden laphoz tartozik egy számláló.
 *   Minden óramegszakításnál minden lap R bitjét hozzáadjuk a számlálójához.
 *   A legkisebb számlálójú lapot dobjuk el.
 *
 * Óramegszakítás helyett miden 5. memóriahivatkozásnál adjuk a számlálóhoz az R bitet.
 *
 * @author Kádár István
 */
public class PageReplaceNFU implements PageReplaceStrategy {

    /** Megmutatja hány hivatkozásonként történjen az R bitek hozzáadása a számlálóhoz. */
    private final int INTERVAL = 5;

    /** A változó számolja hogy hányszor történt hivatkozás a lapra. */
    private int refCount = 0;

    /** Egyedüli példány. */
    private static PageReplaceNFU instance = null;

    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        // minmumkiválasztás a lapok counter mezője alapján
        Page min = physMem.get(0);
        for (int i = 1; i < physMem.size(); i++) {      // elég az 1.-től indulni
            if (physMem.get(i).getCounter() < min.getCounter()) {
                min = physMem.get(i);
            }
        }

        return min;
    }


    /**
     * Adminisztratív tevékenységek lapról történő olvasáskor.
     * A számláló növelése és az R bitek nullázása minden
     * INTERVAL-adik memóriahivatkozás után történik.
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnRead(Page used, LinkedList<Page> physMem) {
        used.setRef(true);
        ++refCount;
        if (refCount == INTERVAL) {                          // minden 5. hivatkozás után
            Page page;
            for (int i = 0; i < physMem.size(); i++) {
                page = physMem.get(i);
                if (page.getRef()) {                        // R bit hozzáadása a számlálóhoz
                   page.incCounter();
                   page.setRef(false);                      // R bit törlése
                }
            }
            refCount = 0;
        }
    }

    /**
     * Adminisztratív tevékenységek lapra történő íráskor.
     * A számláló növelése és az R bitek nullázása minden
     * INTERVAL-adik memóriahivatkozás után történik.
     *
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnWrite(Page used, LinkedList<Page> physMem) {
        used.setRef(true);
        ++refCount;
        if (refCount == 5) {                                // minden 5. hivatkozás után

            Page page;
            for (int i = 0; i < physMem.size(); i++) {
                page = physMem.get(i);
                if (page.getRef()) {                        // R bit hozzáadása a számlálóhoz
                   page.incCounter();
                   page.setRef(false);                      // R bit törlése
                }
            }
            refCount = 0;
        }
    }

    /**
     * Adminisztratív tevékenységek lapcserénél.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem) {
        
    }


    /** Üres konstruktor */
    private PageReplaceNFU() {

    }

    /**
     * Singleton
     * @return példány
     */
    public static PageReplaceNFU getInstance() {
        if (instance == null) {
            instance = new PageReplaceNFU();
        }
        return instance;
    }

}
