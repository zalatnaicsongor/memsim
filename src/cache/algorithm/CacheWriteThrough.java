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
public class CacheWriteThrough implements CacheWriteStrategy {
    private static CacheWriteThrough instance = null;

    public void onDiscard(CacheRow cr) {
    }

    private CacheWriteThrough() {

    }

    public static CacheWriteThrough getInstance() {
        if (CacheWriteThrough.instance == null) {
            CacheWriteThrough.instance = new CacheWriteThrough();
        }
        return CacheWriteThrough.instance;
    }

    public void writeByte(int address, int data) {
        int line = Cache.getInstance().genLine(address);
        int tag = Cache.getInstance().genTag(address);
        int displacement = Cache.getInstance().genDisplacement(address);

        CacheRow row = null;

        try {
            row = Cache.getInstance().getLine(line).getRowByTag(tag);
            row.writeByte(displacement, data);
        } catch (CacheRowNotFoundException e) {
            System.out.println("Adott cím nem volt cache-ben");
        }
        Memory.getInstance().writeByte(address, data);
    }

}
