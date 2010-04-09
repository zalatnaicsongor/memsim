package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.*;

/**
 * Aging lapcserélő algolritmus.
 *   Mint az NFU, de:
 *   Először a számlálót 1 bittel jobbra toljuk, majd R-t nem a jobb,
 *   hanem a baloldali bithez adjuk hozzá.
 *
 * @author Kádár István
 */
public class PageReplaceAging implements PageReplaceStrategy {

    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        // minmumkiválasztás a lapok counter mezője alapján
        Page min = physMem.get(0);
        for (int i = 1; i < physMem.size(); i++) {
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
     * Minden lap számlálóját 1-gyel jobbra toljuk és Ref-et a baloldali
     * bithez adjuk.
     * Tannenbaum könyvében úgy szerepel, hogy ez az esemény minden óra-
     * megszakításnál következik be. Knapp G. - Adamis G. Operációs rend-
     * szerek c. könyve szerint azonban lapcserekor történik a számláló modo-
     * sítása. Az utóbbinál maradunk.
     */
    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem) {
        long counter;                                   // 64 bites számláló
        for (int i = 0; i < physMem.size(); i++) {
            counter = physMem.get(i).getCounter();
            counter >>>= 1;
            if (physMem.get(i).getRef()) {
                counter ^= 1 << 62;
            }
            physMem.get(i).setCounter(counter);
        }
    }

}
