/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memsim_java;

/**
 * Itt csak tesztelem a virtuális memóriát, lapcserélő algoritmusokat.
 * @author ktorpi
 */
public class Main2 {

    public static void main(String[] args) {
        Memory.createMemory();
        Memory memory = Memory.getInstance();
        final int size = Memory.PAGESIZE;

        System.out.println("Memória: " + memory.getPageFrames() + "\n");
        for (int i = 1; i <= 20; i++) {
            int rand = (int)(Math.random() * VirtMemory.NUMBEROFPAGES);
            if (Math.random() < 0.5) {
                memory.readByte(size * rand);
            } else {
                memory.writeByte(size * rand, 345);
            }
            System.out.println(memory.getPageFrames() + "\n");
        }


    }

}
