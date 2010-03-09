package memsim_java;

import java.util.*;

public class Memory {

    private int size;
    private int maxContFreeSpace;
    private int freeSpace;
    private ArrayList<Integer> data;
    private static Memory instance;
    public ArrayList<Pointer> pointers = new ArrayList<Pointer>();

    public void compact() {
        int kezdocim = 0;

        java.util.Collections.sort(this.pointers);
        for (Pointer ptr : this.pointers) {
            if (ptr.getAddress() != kezdocim) {
                ptr.move(kezdocim);
            }
            kezdocim = ptr.getAddress() + ptr.getSizeInBytes();
        }
        System.out.println("Kompaktáltam");
        this.updateContFreeSpace();
    }

    public int readByte(int address) {
        return data.get(address);
    }

    public void writeByte(int address, int data) {
        this.data.add(address, data);
    }

    public Pointer allocPointer(int wordCount) throws MemorySpaceException {
        if (wordCount < 1) {
            throw new MemorySpaceException("1-nél kisebb pointert nem allokálunk!");
        }
        int kezdocim = 0;
        int szabadmeret = 0;
        int meret = wordCount * 2;
        boolean vanHely = false;
        Pointer retval;

        if (meret > this.freeSpace || meret > this.size) {
            throw new MemorySpaceException("Nincs elég memória");
        }
        if (this.getMaxContFreeSpace() < meret) {
            this.compact();
        }

        java.util.Collections.sort(pointers);

        for (Pointer ptr : pointers) {
            szabadmeret = ptr.getAddress() - kezdocim;
            if (szabadmeret >= meret) {
                vanHely = true;
                break;
            }
            kezdocim = ptr.getAddress() + ptr.getSizeInBytes();
        }

        if (!vanHely) {
            szabadmeret = (this.size) - kezdocim;
            if (szabadmeret < meret) {
                throw new MemorySpaceException("Valami nagy baj van!");
            }
        }

        retval = new Pointer(wordCount, kezdocim);
        this.pointers.add(retval);
        this.freeSpace -= meret;
        this.updateContFreeSpace();
        return retval;
    }

    public void updateContFreeSpace() {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int kezdoCim = 0;
        int szabadMeret = 0;
        for (Pointer ptr : pointers) {
            szabadMeret = ptr.getAddress() - kezdoCim;
            temp.add(szabadMeret);
            kezdoCim = ptr.getAddress() + ptr.getSizeInBytes();
        }
        temp.add((this.size) - kezdoCim); //Vége és az utolsó ptr közötti méret
        this.setMaxContFreeSpace(java.util.Collections.max(temp));
        System.out.println("Legnagyobb szabad lyuk: " + this.getMaxContFreeSpace() + " byte");
    }

    protected Memory(int size) {
        this.size = size;
        this.maxContFreeSpace = size;
        this.data = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            data.add(0); // inicializáljuk a memóriát!
        }
        this.freeSpace = size;
    }

    public Pointer getPointer(int address) {
        for (Pointer ptr : this.pointers) {
            if (ptr.getAddress() == address) {
                return ptr;
            }
        }
        return null;
    }

    public int getSize() {
        return this.size;
    }

    public void setMaxContFreeSpace(int maxContFreeSpace) {
        this.maxContFreeSpace = maxContFreeSpace;
    }

    public int getMaxContFreeSpace() {
        return this.maxContFreeSpace;
    }

    public void setFreeSpace(int freeSpace) {
        this.freeSpace = freeSpace;
    }

    public int getFreeSpace() {
        return this.freeSpace;
    }

    public static void createMemory(int size) {
        Memory.instance = new Memory(size);
    }

    public static Memory getInstance() {
        return Memory.instance;
    }
}
