/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memsim_java;

/**
 *
 * @author zalatnaicsongor
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Memory.createMemory(20);
        Pointer ptr1 = null;
        Pointer ptr2 = null;
        Pointer ptr3 = null;
        Pointer ptr4 = null;
        Pointer ptr5 = null;
        try {
            ptr1 = Memory.getInstance().allocPointer(5);
            ptr2 = Memory.getInstance().allocPointer(1);
            ptr3 = Memory.getInstance().allocPointer(1);
            ptr4 = Memory.getInstance().allocPointer(1);
            ptr5 = Memory.getInstance().allocPointer(2);
            ptr4 = ptr4.free();
            ptr2 = ptr2.free();
            ptr4 = Memory.getInstance().allocPointer(2);
        } catch (MemorySpaceException e) {
            System.out.println(e);
        }
        try {
            ptr1.write(0, 65342);
            ptr1.write(1, 65342);
            System.out.println(ptr1.read(0));
        } catch (PointerOutOfRangeException e) {
            System.out.println(e);
        }
    }
}
