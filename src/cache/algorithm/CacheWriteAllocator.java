/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cache.algorithm;
import memsim_java.*;

/**
 *
 * @author zalatnaicsongor
 */
public class CacheWriteAllocator implements CacheWriteStrategy {
    private static CacheWriteAllocator instance = null;

    public void onDiscard(CacheRow cr) {
        cr.writeBack();
    }

    private CacheWriteAllocator() {

    }

    public static CacheWriteAllocator getInstance() {
        if (CacheWriteAllocator.instance == null) {
            CacheWriteAllocator.instance = new CacheWriteAllocator();
        }
        return CacheWriteAllocator.instance;
    }

    public void writeByte(int address, int data) {
        int line = Cache.getInstance().genLine(address);
        int tag = Cache.getInstance().genTag(address);
        int displacement = Cache.getInstance().genDisplacement(address);

        CacheRow row = null;

        try {
            row = Cache.getInstance().getLine(line).getRowByTag(tag);
        } catch (CacheRowNotFoundException e) {
            row = Cache.getInstance().getLine(line).createRow(tag);
            System.out.println("Adott cím nem volt cache-ben");
        }
        row.writeByte(displacement, data);
    }

}
