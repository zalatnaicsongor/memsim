package memsim_java;

/**
 * a memória egy allokált részére mutató pointert megvalósító osztály
 */
public class Pointer implements Comparable {

    private int size; // SZAVAK SZÁMA
    private int address; // KEZDŐCÍM
    private Memory memObj;

    /**
     * a Comparable interfész implementálása
     * @param o
     * @return int
     */
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

    /**
     * egy szót olvas offset-től
     * @param offset
     * @return int
     * @throws PointerOutOfRangeException
     */
    public int read(int offset) throws PointerOutOfRangeException {
        if (offset + 1 > size) {
            throw new PointerOutOfRangeException("Nincs ennyi memória foglalva");
        }
        int msb = readByte(offset * 2);
        int lsb = readByte((offset * 2) + 1);
        return (msb << 8) | lsb;
    }

    /**
     * egy szót ír offset-hez
     * @param offset - hova
     * @param data - mit
     * @throws PointerOutOfRangeException
     */
    public void write(int offset, int data) throws PointerOutOfRangeException {
        if (offset + 1 > size) {
            throw new PointerOutOfRangeException("Nincs ennyi memória foglalva");
        }
        int lsb = data & 0xFF;
        int msb = (data & 0xFF00) >>> 8;
        writeByte(offset * 2, msb);
        writeByte((offset * 2) + 1, lsb);
    }

    /**
     * byte írása offsethez
     * (erre bontja a read is)
     * @param offset
     * @return int
     */
    private int readByte(int offset) {
        return Cache.getInstance().readByte(this.address + offset);
    }

    /**
     * byte írása offset-hez
     * (erre bontja write is)
     * @param offset - hova
     * @param data - mit
     */
    private void writeByte(int offset, int data) {
        Cache.getInstance().writeByte(address + offset, data);
    }

    /**
     * konstruktor
     * @param size - mekkora helyre mutasson a pointer
     * @param startAddress - honnan kezdődjön
     */
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

    /**
     * pointer felszabadítása
     * @return
     */
    public Pointer free() {
        this.clearCache();
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

    /**
     * cache kiürítése
     */
    private void clearCache() {
        for (int i = 0; i < this.getSizeInBytes(); i++) {
            Cache.getInstance().destroyByAddress(this.getAddress() + i);
        }
    }

    /**
     * pointer mozgatása címre
     * @param toAbsoluteAddress - ide
     */
    public void move(int toAbsoluteAddress) {
        this.clearCache();
        for (int i = 0; i < this.getSizeInBytes(); i++) {
            this.memObj.writeByte(toAbsoluteAddress + i, this.memObj.readByte(this.getAddress() + i));
        }
        this.setAddress(toAbsoluteAddress);
    }

    /**
     * pointer mérete
     * @return size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * pointer mérete byte-okban
     * @return size
     */
    public int getSizeInBytes() {
        return (this.size * 2);
    }

    /**
     * cím beállítása
     * @param address
     */
    public void setAddress(int address) {
        this.address = address;
    }

    /**
     * cím lekérése
     * @return address
     */
    public int getAddress() {
        return this.address;
    }
}