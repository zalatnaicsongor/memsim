package memsim_java;
import cache.algorithm.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import virtmemory.algorithm.*;
/**
 *
 * @author zalatnaicsongor
 */
public class Main {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Szerkezet:
        //args-ban kap egy file-t, amiber a műveletek előre le vannk generálva
        //strategy-ket, hogy ebben a futásban mit használ
        //mem, cache és virt mértetket
        //ezek alapján lefut, és a végén a statisztikát adott formában file-ba írja
        //ebből egy másik osztály/program/script grafikont csinálhat...

        //memória belövése
        Memory.createMemory();
        Memory memoria = Memory.getInstance(); // debugger miatt
        VirtMemory virtMem = VirtMemory.getInstance(); //ez is debugger miatt
        //memória belövésének vége
        //cache belövése
        try {
            Cache.create(32, 32, 2);
        } catch (Exception e) {
            System.out.println(e);
        }
        Cache cache = Cache.getInstance(); //debugger miatt
        //cache belövésének vége

        //algoritmusok beállítása
        Cache.getInstance().setRowDiscardStrategy(CacheRowDiscardLRU.getInstance());
        Cache.getInstance().setWriteStrategy(CacheWriteBack.getInstance());
        VirtMemory.getInstance().setPageReplacer(PageReplaceSecondChance.getInstance());
        //algoritmusok beállításának vége



        //segédváltozók
        Random rand = new Random();
        //segédváltozók vége

        //nagy teszt
        Pointer nagyP = null;
        try {
            nagyP = Memory.getInstance().allocPointer(9000);
            for (int i = 0; i < nagyP.getSize(); i++) {
                nagyP.write(i, rand.nextInt(65000));
            }
        } catch (MemorySpaceException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PointerOutOfRangeException e) {
            System.out.println(e);
        }
        //nagy teszt vége



        //csv exportálása
        Statistics.getInstance().exportCSV("nagytomb.csv");
        //kilépés
        System.exit(0);

    }
}
