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
        Memory.createMemory(65536);
        Memory memoria = Memory.getInstance();
        try {
            Cache.create(32, 8, 1);
        } catch (Exception e) {
            System.out.println(e);
        }
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
            ptr1.write(0, 65342);
            ptr1.write(1, 65342);
            ptr2.write(0, 65342);
            ptr5.write(601, 65342);
            ptr5.write(6010, 65342);
            ptr5.write(21675, 65342);
            System.out.println(ptr1.read(0));
            Cache cache = Cache.getInstance();
            ptr1 = ptr1.free();
        } catch (PointerOutOfRangeException e) {
            System.out.println(e);
        }
    }
}
