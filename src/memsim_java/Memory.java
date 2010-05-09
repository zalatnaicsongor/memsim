package memsim_java;

import java.util.*;


/**
 *
 *
 * @author Bán Dénes, Kádár István, Zalatnai Csongor
 */
public class Memory {

    /**
     * Virtuális címtartomány.
     */
    public final int ADDRESSLENGTH;
    
    /**
     * A fizikai címtartomány
     */
    public final int PHYSADDRESSLENGTH;

    /**
     * Az egész memória mérete bájtokban.
     */
    public final int SIZE;
   
    /**
     * A lapméret bájtokban.
     */
    public final int PAGESIZE;
    /*
     * Néhány lehetőség ha lapmérettel akarunk variálni (16 bites
     * cimtartomány esetén):
     *      16 KB   4 lap
     *      4 KB    16 lap
     *      2 KB    32 lap
     *      512 B   128 lap
     *      256 B   256 lap
     */

   /**
     * A lapkeretek száma.
     */
    public final int NUMBEROFPAGEFRAMES;

    private int maxContFreeSpace;
    private int freeSpace;

    /**
     * A lapkeretek láncolt listája.
     */
    private LinkedList<Page> pageFrames;
    
    private static Memory instance;
    public ArrayList<Pointer> pointers = new ArrayList<Pointer>();

    /**
     * A memory objecthez tartozó virtuális memória object.
     */
    private VirtMemory virtMem;

	/**
	 * Végignézi a fizikai memóriában lévő lapokat.
	 * Ha benne van az, aminek megfelelő a lapcíme, akkor visszaadja.
	 * Ha nincs, akkor PageFault-ot dob.
	 * 
	 * @author zealot
	 */
	public Page getPageFromPhysicalMemory(int pageNumber) throws PageFaultException {
		Iterator<Page> it = pageFrames.iterator();
        Page ret = null;
		while (it.hasNext()) {
			Page p = it.next();
			if (p.getPageNumber() == pageNumber) {
				ret = p;
			}
		}
        if (ret == null) {
    	    throw new PageFaultException("A lap nincs a fizikai memóriában!");
        }
        return ret;
	}

    /**
     * kompaktálja a memóriát
     */
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
        Statistics.getInstance().addCompact();
        this.updateContFreeSpace();
    }

    /**
     * egy byte-ot olvas address-ről
     * @param address
     * @return byte
     */
    public int readByte(int address) {
        boolean back = true;
        
        int pageNumber = address / PAGESIZE;            // melyik lapon van a cím
        int physicalAddress = address % PAGESIZE;       // a lapon hol
		Page hereItIs = null;
        do {
            try {
                //ha bennvan, akkor béke, ha nem, akkor hibát dob
                hereItIs = getPageFromPhysicalMemory(pageNumber);
                back = false;
            } catch (PageFaultException pf) {
                if (pageFrames.size() == NUMBEROFPAGEFRAMES) {  // akkor lapcsere
                    // lekönyveljük, hogy lapcsere fog történni
                    virtMem.getPageReplacer().doTheAccountingOnPageReplace(pageFrames);
                    // amelyik lapot kidobjuk
                    Page out = virtMem.getPageReplacer().whichToThrowOut(pageFrames);
                    // kidobjuk
                    // HA DIRTY, AKKOR VISSZA KÉNE ÍRNI A LEMEZRE
                    // ENNEK IDEJE: AHÁNY BYTE VAN A LAPBAN * ELÉRÉS
                    if (out.getDirty()) {
                        Statistics.getInstance().useVirtualWrite();
                    }
                    virtMem.throwOutPage(out);
                }
                // Ha már van hely, akkor jöhet az új lap, a pageNumber-adik
                virtMem.loadPageIntoMemory(pageNumber);
                
                Statistics.getInstance().useVirtualRead();
            }
        } while (back);
        
        Statistics.getInstance().useMemory("read");

        //az alg.-nak megfelelő módon lekönyveljük, hogy ezt most OLVASTUK
        virtMem.getPageReplacer().doTheAccountingOnRead(hereItIs, pageFrames);
		return hereItIs.readByte(physicalAddress);
	
    }

    /**
     * egy byte írása address címre
     * @param address
     * @param data
     */
    public void writeByte(int address, int data) {
        boolean back = true;
        boolean isThereReplace = false;                 // kellett-e lapcsere

        int pageNumber = address / PAGESIZE;            // melyik lapon van a cím
        int physicalAddress = address % PAGESIZE;       // a lapon hol
		Page hereItIs = null;
        do {
            try {
                //ha bennvan, akkor béke, ha nem, akkor hibát dob
                hereItIs = getPageFromPhysicalMemory(pageNumber);
                back = false;
            } catch (PageFaultException pf) {
                if (pageFrames.size() == NUMBEROFPAGEFRAMES) {
                    // amelyik lapot kidobjuk
                    Page out = virtMem.getPageReplacer().whichToThrowOut(pageFrames);
                    // HA DIRTY, AKKOR VISSZA KÉNE ÍRNI A LEMEZRE
                    // ENNEK IDEJE: AHÁNY BYTE VAN A LAPBAN * ELÉRÉS
                    if (out.getDirty()) {
                        Statistics.getInstance().useVirtualWrite();
                    }
                    // kidobjuk
                    virtMem.throwOutPage(out);
                    // lapcsere fog töténni
                    isThereReplace = true;
                }
                // jöhet az új lap
                virtMem.loadPageIntoMemory(pageNumber);
                // ha lapcsere történt lekönyveljük
                if (isThereReplace)
                    virtMem.getPageReplacer().doTheAccountingOnPageReplace(pageFrames);
                isThereReplace = false;

                Statistics.getInstance().useVirtualRead();
            }
        } while (back);
        Statistics.getInstance().useMemory("write");

        //az alg.-nak megfelelő módon lekönyveljük, hogy ezt most ÍRTUK
        virtMem.getPageReplacer().doTheAccountingOnWrite(hereItIs, pageFrames);
		hereItIs.writeByte(physicalAddress, data);
    }

    /**
     * egy poiter allokálása
     * @param wordCount - mennyit
     * @return - a pointer
     * @throws MemorySpaceException
     */
    public Pointer allocPointer(int wordCount) throws MemorySpaceException {
        if (wordCount < 1) {
            throw new MemorySpaceException("1-nél kisebb pointert nem allokálunk!");
        }
        int kezdocim = 0;
        int szabadmeret = 0;
        int meret = wordCount * 2;
        boolean vanHely = false;
        Pointer retval;

        if (meret > this.freeSpace || meret > SIZE) {
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
            szabadmeret = (SIZE) - kezdocim;
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

    /**
     * frissíti, hogy mennyi összefüggő szabad hely van a memóriában
     */
    public void updateContFreeSpace() {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int kezdoCim = 0;
        int szabadMeret = 0;
        for (Pointer ptr : pointers) {
            szabadMeret = ptr.getAddress() - kezdoCim;
            temp.add(szabadMeret);
            kezdoCim = ptr.getAddress() + ptr.getSizeInBytes();
        }
        temp.add((SIZE) - kezdoCim); //Vége és az utolsó ptr közötti méret
        this.setMaxContFreeSpace(java.util.Collections.max(temp));
        System.out.println("Legnagyobb szabad lyuk: " + this.getMaxContFreeSpace() + " byte");
    }

    /**
     * a memória beállítása konstruktorban
     * @param addresslength - címméret
     * @param physaddresslength - fizikai címméret
     * @param pagesize - lapméret
     * @param prs - a lapcserélő str. is ide kell, mert ő hozza lérte a virtuális memóriát is
     */
    protected Memory(int addresslength, int physaddresslength, int pagesize, PageReplaceStrategy prs) {

        this.ADDRESSLENGTH = addresslength;
        this.PHYSADDRESSLENGTH = physaddresslength;
        this.PAGESIZE = pagesize;

        this.SIZE = (int) Math.pow(2, ADDRESSLENGTH);
        this.NUMBEROFPAGEFRAMES = (int)(Math.pow(2, PHYSADDRESSLENGTH)) / PAGESIZE;


        this.maxContFreeSpace = SIZE;
    
        // virtuálsi memória felépitése
        int pagenum = this.SIZE / this.PAGESIZE;
        VirtMemory.createVirtMemory(prs, pagenum, this.PAGESIZE);
        virtMem = VirtMemory.getInstance();

        // a lapkeretek létrehozása, kezdetben nincsenek bennt lapok
        pageFrames = new LinkedList<Page>();
        // ELSŐ n lap betöltése memóriába, hogy az ne számítson a statisztikába!
        // vagyis minek vegyük lemezről olvasásnak azt, ami már eleve befért a memóriába, csak kezdetkor nem töltöttük be?
        for (int i = 0; i< this.NUMBEROFPAGEFRAMES; i++) {
            pageFrames.add(virtMem.getPages().get(i));
        }

        this.freeSpace = SIZE;
    }

    /**
     * visszaad egy pointert cím alapján
     * (vagy ha nincs ilyen, akkor null)
     * @param address
     * @return pointer
     */
    public Pointer getPointer(int address) {
        for (Pointer ptr : this.pointers) {
            if (ptr.getAddress() == address) {
                return ptr;
            }
        }
        return null;
    }

    /**
     * max. egybefüggő hely setter
     * @param maxContFreeSpace
     */
    public void setMaxContFreeSpace(int maxContFreeSpace) {
        this.maxContFreeSpace = maxContFreeSpace;
    }

    /**
     * max egybefüggő hely getter
     * @return max
     */
    public int getMaxContFreeSpace() {
        return this.maxContFreeSpace;
    }

    /**
     * összes szabad hely setter
     * @param freeSpace
     */
    public void setFreeSpace(int freeSpace) {
        this.freeSpace = freeSpace;
    }

    /**
     * összes szabad hely getter
     * @return freeSpace
     */
    public int getFreeSpace() {
        return this.freeSpace;
    }

    /**
     * a fizikai memóriában lévő lapok
     * @return list
     */
    public LinkedList<Page> getPageFrames() {
        return pageFrames;
    }

    /**
     * memória elkészítése
     * @param addresslength
     * @param physaddresslength
     * @param pagesize
     * @param prs
     */
    public static void createMemory(int addresslength, int physaddresslength, int pagesize, PageReplaceStrategy prs) {
        Memory.instance = new Memory(addresslength, physaddresslength, pagesize, prs);
    }

    /**
     * a memória példány lekérése
     * @return instance
     */
    public static Memory getInstance() {
        return Memory.instance;
    }
}
