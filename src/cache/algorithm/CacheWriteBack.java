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
public class CacheWriteBack implements CacheWriteStrategy {
    private static CacheWriteBack instance = null;

    public void onDiscard(CacheRow cr) {
        cr.writeBack();
    }

    private CacheWriteBack() {

    }

    public static CacheWriteBack getInstance() {
        if (CacheWriteBack.instance == null) {
            CacheWriteBack.instance = new CacheWriteBack();
        }
        return CacheWriteBack.instance;
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
            Memory.getInstance().writeByte(address, data);
            System.out.println("Adott cím nem volt cache-ben");
        }
    }

}
