/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memsim_java;
import cache.algorithm.*;
/**
 *
 * @author zalatnaicsongor
 */
public class Main {

    //ennyi ideig tart hozzájuk fordulni
    public static final int cacheTime = 1;
    public static final int memoryTime = 10;
    public static final int virtualTime = 100;

    //ennyiszer fordultunk hozzájuk ebben a futásban
    public static int cacheUsed = 0;
    public static int memoryUsed = 0;
    public static int virtualUsed = 0;

    //ennyiszer történt hiba
    public static int cacheFault = 0;
    public static int pageFault = 0;

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

        Memory.createMemory();
        Memory memoria = Memory.getInstance();
        try {
            Cache.create(32, 32, 2);
        } catch (Exception e) {
            System.out.println(e);
        }
        Cache cache = Cache.getInstance();
        cache.setRowDiscardStrategy(CacheRowDiscardLRU.getInstance());
        Pointer ptr1 = null;
        Pointer ptr2 = null;
        Pointer ptr3 = null;
        Pointer ptr4 = null;
        Pointer ptr5 = null;
        Pointer ptr6 = null;
        Pointer ptr7 = null;
        try {
            ptr1 = Memory.getInstance().allocPointer(5);
            ptr2 = Memory.getInstance().allocPointer(1);
            ptr3 = Memory.getInstance().allocPointer(1);
            ptr4 = Memory.getInstance().allocPointer(1);
            ptr6 = Memory.getInstance().allocPointer(2);
            ptr5 = Memory.getInstance().allocPointer(30000);
            ptr6 = ptr6.free();
            ptr7 = Memory.getInstance().allocPointer(3);
        } catch (MemorySpaceException e) {
            System.out.println(e);
        }
        try {
            ptr5.write(601, 65342);
            ptr5.write(610, 65342);
            ptr5.write(6010, 65342);
            ptr5.write(8057, 65342);
            ptr5.write(4996, 65342);
            ptr1 = ptr1.free();
        } catch (PointerOutOfRangeException e) {
            System.out.println(e);
        }
    }
}
