package memsim_java;

public class Pointer implements Comparable {

    private int size; // SZAVAK SZÁMA
    private int address; // KEZDŐCÍM
    private Memory memObj;

    public int compareTo(Object o) {
        Pointer p1 = (Pointer) o;
        int retval = 0;
        if (this.getAddress() > p1.getAddress()) {
            retval = 1;
        } else if (this.getAddress() < p1.getAddress()) {
            retval = -1;
        }
        return retval;
    }

    public int read(int offset) throws PointerOutOfRangeException {
        if (offset + 1 > size) {
            throw new PointerOutOfRangeException("Nincs ennyi memória foglalva");
        }
        int msb = readByte(offset * 2);
        int lsb = readByte((offset * 2) + 1);
        return (msb << 8) | lsb;
    }

    public void write(int offset, int data) throws PointerOutOfRangeException {
        if (offset + 1 > size) {
            throw new PointerOutOfRangeException("Nincs ennyi memória foglalva");
        }
        int lsb = data & 0xFF;
        int msb = (data & 0xFF00) >>> 8;
        writeByte(offset * 2, msb);
        writeByte((offset * 2) + 1, lsb);
    }

    private int readByte(int offset) {
        return Cache.getInstance().readByte(this.address + offset);
    }

    private void writeByte(int offset, int data) {
        Cache.getInstance().writeByte(address + offset, data);
    }

    public Pointer(int size, int startAddress) {
        this.size = size;
        this.address = startAddress;
        this.memObj = Memory.getInstance();
        System.out.println("-----");
        System.out.println("Allokáltam egy pointert!");
        System.out.println("Méret:" + this.getSizeInBytes() + " byte");
        System.out.println("Kezdőcím: " + address);
        System.out.println("Végcím: " + (address + this.getSizeInBytes() - 1));
    }

    public Pointer free() {
        this.memObj.pointers.remove(this);
        this.memObj.setFreeSpace(this.memObj.getFreeSpace() + this.getSizeInBytes());
        System.out.println("-----");
        System.out.println("Deallokáltam egy pointert!");
        System.out.println("Méret:" + this.getSizeInBytes() + " byte");
        System.out.println("Kezdőcím: " + address);
        System.out.println("Végcím: " + (address + this.getSizeInBytes() - 1));
        this.memObj.updateContFreeSpace();
        return null;
    }

    public void move(int toAbsoluteAddress) {
        for (int i = 0; i < this.getSizeInBytes(); i++) {
            this.memObj.writeByte(toAbsoluteAddress + i, this.memObj.readByte(this.getAddress() + i));
        }
        this.setAddress(toAbsoluteAddress);
    }

    public int getSize() {
        return this.size;
    }

    public int getSizeInBytes() {
        return (this.size * 2);
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getAddress() {
        return this.address;
    }
}
