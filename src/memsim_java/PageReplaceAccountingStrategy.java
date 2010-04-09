package memsim_java;

import java.util.*;

/**
 * Ez ahhoz kell, hogy ha használunk egy lapot, akkor az megfelelően lekönyvelődjön
 * (pl.: dirty és hivatkozott bitek beállitása, usageCount++, linkedlist elejére kerül...)
 *
 * @author
 */
public interface PageReplaceAccountingStrategy {
    
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
     * Adminisztratív tevékenységek lapcserénél.
     * @param physMem A lapkeretek láncolt listája.
     */
    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem);

}
