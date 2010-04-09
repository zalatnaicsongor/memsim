package memsim_java;

import java.util.LinkedList;

/**
 * Minden lapcserélő interfésze
 * @author zealot
 */
public interface PageReplaceStrategy {

    /**
     * ez visszaadja, hogy ez az alg. szerint melyiket kell kidobni...
     * @param physMem
     * @return
     */
    public Page whichToThrowOut(LinkedList<Page> physMem);
    
    /**
     * Adminisztratív tevékenységek lapról történő olvasáskor.
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnRead(Page used, LinkedList<Page> physMem);

    /**
     * Adminisztratív tevékenységek lapa történő íráskor.
     * @param used A kérdéses lap.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnWrite(Page used, LinkedList<Page> physMem);

    /**
     * Adminisztratív tevékenységek lapcserénél. (Néhány algoritmusnál szükséges.)
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem);
}
