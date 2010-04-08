package memsim_java;

import java.util.LinkedList;

/**
 * Minden lapcserélő interfésze
 * @author zealot
 */
public interface PageReplacer {
    /**
     * Ez ahhoz kell, hogy ha használunk egy lapot, akkor az megfelelően lekönyvelődjön
     * (usageCount++, linkedlist elejére kerül...)
     * @param PhysMem
     */
    public void doTheAccounting(Page used, LinkedList<Page> PhysMem);

    /**
     * ez meg visszaadja, hogy ez az alg. szerint melyiket kell kidobni...
     * @param PhysMem
     * @return
     */
    public Page whichToThrowOut(LinkedList<Page> PhysMem);
}
