/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memsim_java;

/**
 *
 * @author zalatnaicsongor
 */
public interface CacheWriteStrategy {
    public void writeByte(int address, int data);
    public void onDiscard(CacheRow cr);
}
