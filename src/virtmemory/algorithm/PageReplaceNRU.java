package virtmemory.algorithm;

import java.util.*;
import memsim_java.*;

/**
 * NRU lapcserélő algolritmus.
 *   Minden laphoz R (hivatkozott) és M (módosított) bitek tartoznak.
 *   Adott időközönként nullázzuk az R bitet.
 *   4 osztály:
 *      1. nem R, nem M
 *      2. nem R, M
 *      3. R, nem M
 *      4. R, M
 *   A legkisebb sorszámú nemüres osztályból választjuk ki véletlenszerűen
 *   az eldobandó lapot.
 *
 * @author Kádár István
 */
public class PageReplaceNRU implements PageReplaceStrategy {

    /** Egyedüli példány. */
    private static PageReplaceNRU instance = null;

    /**
     * 4 ArrayList a 4 osztálynak megfelelőem.
     */
    ArrayList<Page> notRefNotDirty = new ArrayList<Page>();
    ArrayList<Page> notRefDirty = new ArrayList<Page>();
    ArrayList<Page> refNotDirty = new ArrayList<Page>();
    ArrayList<Page> refDirty = new ArrayList<Page>();

    /**
     * Minden lap besorolása valmelyik osztályba.
     * @param physMem Lapkeretek láncolt listája.
     */
    private void assort(LinkedList<Page> physMem) {
        Page page;                                      // segédlap
        for (int i = 0; i < physMem.size(); i++) {
            page = physMem.get(i);
            if (!page.getRef() && !page.getRef()) {
                notRefNotDirty.add(page);               // 1. osztályhoz adjuk
            } else if (!page.getRef() && page.getDirty()) {
                notRefDirty.add(page);                  // 2. osztályhoz
            } else if (page.getRef() && !page.getDirty()) {
                refNotDirty.add(page);                  // 3. osztályhoz
            } else if (page.getRef() && page.getDirty()) {
                refDirty.add(page);                     // 4. osztályhoz
            }
        }
    }

    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        int rand;                                       // véletlen szám

        assort(physMem);                                // szétválogatjuk a lapokat
        if (! notRefNotDirty.isEmpty()) {               // ha nem üres
            rand = (int)(Math.random() * notRefNotDirty.size());
            return notRefNotDirty.get(rand);
        } else if (! notRefDirty.isEmpty()) {
            rand = (int)(Math.random() * notRefDirty.size());
            return notRefDirty.get(rand);
        } else if (! refNotDirty.isEmpty()) {
            rand = (int)(Math.random() * refNotDirty.size());
            return refNotDirty.get(rand);
        } else {
            rand = (int)(Math.random() * refDirty.size());
            return refDirty.get(rand);
        }
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
        used.setDirty(true);
        used.setRef(true);
    }

    /**
     * Adminisztratív tevékenységek lapcserénél.
     * @param physMem A lapkeretek láncolt listája.
     *
     * Nullázzuk a Ref bitet.
     * Tannenbaum könyvében úgy szerepel, hogy ez az esemény minden óra-
     * megszakításnál következik be. Knapp G. - Adamis G. Operációs rend-
     * szerek c. könyve szerint azonban lapcserekor történik a Ref nullázása.
     */
    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem) {
        for (int i = 0; i < physMem.size(); i++) {
            physMem.get(i).setRef(false);
        }
    }

    
    /** Üres konstruktor */
    private PageReplaceNRU() {

    }

    /**
     * Singleton
     * @return példány
     */
    public static PageReplaceNRU getInstance() {
        if (instance == null) {
            instance = new PageReplaceNRU();
        }
        return instance;
    }
}
