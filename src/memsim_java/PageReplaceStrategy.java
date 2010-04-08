package memsim_java;

import java.util.LinkedList;

/**
 * Minden lapcserélő interfésze
 * @author zealot
 */
public interface PageReplaceStrategy {
    /**
     * Ez ahhoz kell, hogy ha használunk egy lapot, akkor az megfelelően lekönyvelődjön
     * (usageCount++, linkedlist elejére kerül...)
     *
     * A Ref és Dirty bitek módosítása nem itt, hanem Page.writeByte Page.readBytban
     * van, mert az írásnál és olvasásnál eltérő.
     * 
     * @param physMem
     */
    public void doTheAccounting(Page used, LinkedList<Page> physMem);

    /**
     * ez meg visszaadja, hogy ez az alg. szerint melyiket kell kidobni...
     * @param physMem
     * @return
     */
    public Page whichToThrowOut(LinkedList<Page> physMem);
}
