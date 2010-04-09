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
}
