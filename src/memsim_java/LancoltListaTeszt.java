
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
public class LancoltListaTeszt {


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
        int numOperations = 300;
        int linkedListSize = 16;
        int linkedListDataSize = linkedListSize - 1;
        ArrayList<Pointer> ptrTarolo = new ArrayList<Pointer>(numOperations);
        Random rand = new Random();
        //segédváltozók vége

        for (int i = 0; i < numOperations; i++) { //Csináljunk láncolt listát
            Pointer temp = null;
            try {
                temp = Memory.getInstance().allocPointer(linkedListSize);
                for (int j = 0; j < temp.getSize() - 1; j++) {
                    temp.write(j, rand.nextInt(Memory.SIZE - 1));
                    //teleírjuk random adattal, de az utolsó szót nem írjuk bele!
                }
            } catch (MemorySpaceException e) {
                System.out.println(e);
            } catch (PointerOutOfRangeException e) {
                System.out.println(e);
            }
            ptrTarolo.add(i, temp);
        }
        for (Pointer ptr : ptrTarolo) {
            try {
                //beállítjuk a címet, ahova ugrani kell
                ptr.write(ptr.getSize() - 1, ptrTarolo.get(rand.nextInt(ptrTarolo.size() - 1)).getAddress());
            } catch (PointerOutOfRangeException ex) {
                Logger.getLogger(LancoltListaTeszt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Pointer most = ptrTarolo.get(0);
        for (int i = 0; i < numOperations; i++) {
            for (int j = 0; j < linkedListDataSize; j++) {
                try {
                    //kiolvassuk a stat kedvéért
                    most.read(j);
                } catch (PointerOutOfRangeException ex) {
                    Logger.getLogger(LancoltListaTeszt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                most = Memory.getInstance().getPointer(most.read(most.getSize() - 1));
            } catch (PointerOutOfRangeException ex) {
                Logger.getLogger(LancoltListaTeszt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }




        //csv exportálása
        Statistics.getInstance().exportCSV("lancoltlista.csv");
        //kilépés
        System.exit(0);

    }

}
