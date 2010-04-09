package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.*;

/**
 * NFU lapcserélő algolritmus.
 *   Minden laphoz tartozik egy számláló.
 *   Minden lapcserénél megnézzük a memóriában lévő lapokat és minden lap
 *      R bitjét hozzáadjuk a számlálójához.
 *   A legkisebb számlálójú lapot dobjuk el.
 *
 * @author Kádár István
 */
public class PageReplaceNFU implements PageReplaceStrategy {

    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        // minmumkiválasztás a lapok counter mezője alapján
        Page min = physMem.get(0);
        for (int i = 1; i < physMem.size(); i++) {      // elég az 1.-től indulni.
            if (physMem.get(i).getCounter() < min.getCounter()) {
                min = physMem.get(i);
            }
        }

        return min;
    }


    /**
     * Adminisztratív tevékenységek lapról történő olvasáskor.
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnRead(Page used, LinkedList<Page> physMem) {
        used.setRef(true);
    }

    /**
     * Adminisztratív tevékenységek lapra történő íráskor.
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnWrite(Page used, LinkedList<Page> physMem) {
        used.setRef(true);
    }

    /**
     * Adminisztratív tevékenységek lapcserénél.
     * @param physMem A lapkeretek láncolt listája.
     *
     * Minden lap Ref bitjét hozzáadjuk a számlálójához.
     * Tannenbaum könyvében úgy szerepel, hogy ez az esemény minden óra-
     * megszakításnál következik be. Knapp G. - Adamis G. Operációs rend-
     * szerek c. könyve szerint azonban lapcserekor történik a számláló modo-
     * sítása. Az utóbbinál maradunk.
     */
    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem) {
        Page page;
        for (int i = 0; i < physMem.size(); i++) {
            page = physMem.get(i);
            if (page.getRef()) {
               page.incCounter();
            }
        }
    }

}
