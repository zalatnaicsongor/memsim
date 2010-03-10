/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memsim_java;
import java.util.*;
/**
 *
 * @author zalatnaicsongor
 */
public class CacheRow {
    private int tag;
    private CacheLine container;
    private boolean valid = true;
    private boolean dirty = false;
    private int useCount = 0;
    private int useSequence = 0;

    private int[] data;

    public int readByte(int displacement) {
        return data[displacement];
    }

    public void writeByte(int displacement, int data) {
        this.setDirty(true);
        this.data[displacement] = data;
    }

    public int getTag() {
        return tag;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public int getUseSequence() {
        return useSequence;
    }

    public void setUseSequence(int useSequence) {
        this.useSequence = useSequence;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void discard() {
        Cache.getInstance().getWriteStrategy().onDiscard(this);
        this.setValid(false);
    }

    public void writeBack() {
        int startAddress = 0;
        startAddress = Cache.getInstance().genAddress(tag, container.getLine(), 0);
        for (int i = 0; i < Cache.getInstance().getRowSize(); i++) {
            Memory.getInstance().writeByte(startAddress + i, this.readByte(i));
        }
    }

    public CacheRow(int tag, CacheLine container) {
        int startAddress = 0;
        this.tag = tag;
        this.container = container;
        this.data = new int[Cache.getInstance().getRowSize()];

        startAddress = Cache.getInstance().genAddress(tag, container.getLine(), 0);
        for (int i = 0; i < Cache.getInstance().getRowSize(); i++) {
            this.data[i] = Memory.getInstance().readByte(startAddress + i);
        }

    }

}
