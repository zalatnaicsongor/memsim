package virtmemory.algorithm;

import java.util.LinkedList;
import memsim_java.*;

/**
 * Random lapcserélő algoritmus.
 *   Véletlenszerűen választja ki a kidobandó lapot.
 *
 * @author Kádár István
 */
public class PageReplaceRandom implements PageReplaceStrategy {
    
    /**
     * Visszadja melyik lapot kell kidobni.
     * @param physMem A lapkeretek láncolt listája.
     * @return A kidobandó lap.
     */
    public Page whichToThrowOut(LinkedList<Page> physMem) {
        // véletlen szám 0-tól physMem.size() - 1 -ig.
        int rand = (int)(Math.random() * physMem.size());
        return physMem.get(rand);
    }

     /*
     * FIXME: nemtudom ezeket implementálni kell-e, a readByte/writeByte miatt?
     * de biztos, hogy nem kell csinálniuk semmit.
     */
    public void doTheAccountingOnRead(Page used, LinkedList<Page> physMem) {
        
    }

    public void doTheAccountingOnWrite(Page used, LinkedList<Page> physMem) {

    }

    public void doTheAccountingOnPageReplace(LinkedList<Page> physMem) {

    }

}
