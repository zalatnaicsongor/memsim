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
    public static final int ADDRESSLENGTH = 16;
    
    /**
     * A fizikai címtartomány legyen 14 bites. A virtuális címtarto-
     * mány negyede.
     */
    public static final int PHYSADDRESSLENGTH = 14;

    /**
     * Az egész memória mérete bájtokban.
     */
    public static final int SIZE = (int) Math.pow(2, ADDRESSLENGTH);    // 65536

    /**
     * A lapméret bájtokban.
     * A lapméret legyen egyenlőre 4 KB-os. Ekkor a virtuális cimtarto-
     * mány 16 lapból, a fizikai pedig 4 lapból áll.
     */
    public static final int PAGESIZE = 4096;
     /* Néhány lehetőség ha lapmérettel akarunk variálni (16 bites
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
    public static final int NUMBEROFPAGEFRAMES = (int)(Math.pow(2, PHYSADDRESSLENGTH)) / PAGESIZE;

    private int maxContFreeSpace;
    private int freeSpace;

    /**
     * A lapkeretek láncolt listája.
     */
    private LinkedList<Page> pageFrames;
    
    // FIXME
    private ArrayList<Integer> data;



    private static Memory instance;
    public ArrayList<Pointer> pointers = new ArrayList<Pointer>();

    /**
     * A memory objecthez tartozó virtuális memória object.
     */
    private VirtMemory virtMem;

    public PageReplaceAccountingStrategy pageReplacer;

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
        int pageNumber = address >>> PHYSADDRESSLENGTH; //felső bites lapcím
		int mask = (~0) >>> (ADDRESSLENGTH - PHYSADDRESSLENGTH);
		int physicalAddress = address & mask; //fizikai cím
		Page hereItIs = null;
		try {
            //ha bennvan, akkor béke, ha nem, akkor hibát dob
			hereItIs = getPageFromPhysicalMemory(pageNumber);
		} catch (PageFaultException pf) {
            if (pageFrames.size() == NUMBEROFPAGEFRAMES) {
			    Page out = pageReplacer.whichToThrowOut(pageFrames);
                //ha megvan, akkor valahogy kidobni
                //lehet hogy ez is alg.függő, és akkor nem itt kellene hogy legyen
                virtMem.throwOutPage(out);
            }
            //Ha már van hely, akkor jöhet az új lap
            /*
             * Mégpedig a pageNumber-adik sorszámú?
             * Ha igen: ÚGYVAN
             */
            virtMem.loadPageIntoMemory(pageNumber);
            Main.virtualUsed++;
            readByte(address); //rekurzívan hívom újra, itt már nem lesz fault
		}
        //az alg.-nak megfelelő módon lekönyveljük, hogy ezt most használtuk
        pageReplacer.doTheAccounting(hereItIs, pageFrames);
        Main.memoryUsed++;
		return hereItIs.readByte(physicalAddress);
	
    }

    public void writeByte(int address, int data) {
        //this.data.add(address, data);
        //ITT HIBA VAN
        //de most itt hagyom, mert nemtom, hogy ha írni akarok és nincs bent
        //akkor is be kell e hozni, vagy hogy
        // de ha igen, akkor u.a., mint a read...
        /*
         * igen, akkor is be kell.
         */

        //nézd át, hogy jó e a logika, de ez csak kopi-pészt szerintem.


        int pageNumber = address >>> PHYSADDRESSLENGTH; //felső bites lapcím
		int mask = (~0) >>> (ADDRESSLENGTH - PHYSADDRESSLENGTH);
		int physicalAddress = address & mask; //fizikai cím
		Page hereItIs = null;
		try {
            //ha bennvan, akkor béke, ha nem, akkor hibát dob
			hereItIs = getPageFromPhysicalMemory(pageNumber);
		} catch (PageFaultException pf) {
            if (pageFrames.size() == NUMBEROFPAGEFRAMES) {
			    Page out = pageReplacer.whichToThrowOut(pageFrames);
                //ha megvan, akkor valahogy kidobni
                //lehet hogy ez is alg.függő, és akkor nem itt kellene hogy legyen
                virtMem.throwOutPage(out);
            }
            //Ha már van hely, akkor jöhet az új lap
            /*
             * Mégpedig a pageNumber-adik sorszámú?
             * Ha igen: ÚGYVAN
             */
            virtMem.loadPageIntoMemory(pageNumber);
            Main.virtualUsed++;
            writeByte(address, data); //rekurzívan hívom újra, itt már nem lesz fault
		}
        //az alg.-nak megfelelő módon lekönyveljük, hogy ezt most használtuk
        pageReplacer.doTheAccounting(hereItIs, pageFrames);
        Main.memoryUsed++;
		hereItIs.writeByte(physicalAddress, data);
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

    protected Memory() {
        this.maxContFreeSpace = SIZE;
    
        // virtuálsi memória felépitése
        virtMem = VirtMemory.getInstance();

        // a lapkeretek létrehozása, kezdetben nincsenek bennt lapok
        pageFrames = new LinkedList<Page>();

//****
        this.data = new ArrayList<Integer>();
        for (int i = 0; i < SIZE; i++) {
            data.add(0); // inicializáljuk a memóriát!
        }
//****

        this.freeSpace = SIZE;
    }

    public Pointer getPointer(int address) {
        for (Pointer ptr : this.pointers) {
            if (ptr.getAddress() == address) {
                return ptr;
            }
        }
        return null;
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

    public LinkedList<Page> getPageFrames() {
        return pageFrames;
    }

    public static void createMemory() {
        Memory.instance = new Memory();
    }

    public static Memory getInstance() {
        return Memory.instance;
    }
}
