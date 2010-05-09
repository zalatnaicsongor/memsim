package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.*;

/**
 * Aging lapcserélő algolritmus.
 *   Mint az NFU, de:
 *   Először a számlálót 1 bittel jobbra toljuk, majd R-t nem a jobb,
 *   hanem a baloldali bithez adjuk hozzá.
 *
 * Minden 5. memóriahivatkozásnál adjuk a számlálóhoz az R bitet.
 *
 * @author Kádár István
 */
public class PageReplaceAging implements PageReplaceStrategy {

    /** Megmutatja hány hivatkozásonként történjen az R bitek hozzáadása a számlálóhoz. */
    private final int INTERVAL = 5;

    /** A változó számolja hogy hányszor történt hivatkozás a lapra. */
    private int refCount = 0;

    /** Egyedüli példány. */
    private static PageReplaceAging instance = null;

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
        long counter;                                       // 64 bites számláló

        used.setRef(true);
        ++refCount;
        if (refCount == INTERVAL) {                         // minden 5. hivatkozás után
            for (int i = 0; i < physMem.size(); i++) {
                counter = physMem.get(i).getCounter();
                counter >>>= 1;
                if (physMem.get(i).getRef()) {
                    counter ^= (1 << 62);
                    physMem.get(i).setRef(false);           // R bit törlése
                }
                physMem.get(i).setCounter(counter);
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
        long counter;                                       // 64 bites számláló

        ++refCount;
        if (refCount == INTERVAL) {                         // minden 5. hivatkozás után
            for (int i = 0; i < physMem.size(); i++) {
                counter = physMem.get(i).getCounter();
                counter >>>= 1;
                if (physMem.get(i).getRef()) {
                    counter ^= (1 << 62);                   // eltolás a végére és hozzáadás
                    physMem.get(i).setRef(false);           // R bit törlése
                }
                physMem.get(i).setCounter(counter);
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
    private PageReplaceAging() {

    }

    /**
     * Singleton
     * @return példány
     */
    public static PageReplaceAging getInstance() {
        if (instance == null) {
            instance = new PageReplaceAging();
        }
        return instance;
    }

}
