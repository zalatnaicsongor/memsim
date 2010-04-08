package memsim_java;

import java.util.*;

/**
 * Virtuális memória osztály.
 *
 * @author Kádár István
 */
public class VirtMemory {

    /**
     * A lapok száma.
     */
    public static final int NUMBEROFPAGES = Memory.SIZE / Memory.PAGESIZE;

    /**
     * Az osztály egyedüli példánya
     */
    private static VirtMemory instance = null;

    /**
     * A lapok, mint a virtuális memória egységei.
     */
    public ArrayList<Page> pages;

    /**
     * A lapkeretek láncolt listája a Memory-ból, a fizikai
     * memória.
     */
    private LinkedList<Page> physMem = Memory.getInstance().getPageFrames();


    /**
     * Konstruktor, amely a lapokat létrehozza és inicializájla.
     */
    protected VirtMemory() {
        pages = new ArrayList<Page>(NUMBEROFPAGES);
        for (int i = 0; i < NUMBEROFPAGES; i++) {
            pages.add(new Page(i));                     // lap létrehozása sorszámával inicializálva
        }
    }


    /**
     * Lap kidobás a memóriából.
     * @param out Az eldobandó lap.
     */
    public void throwOutPage(Page out) {
        if (out.getDirty()) {
            pages.set(out.getPageNumber(), out);        // ha modosított, visszaírjuk a helyére
        }
        physMem.remove(out);                            // törlés a memóriából
    }

    /**
     * Adott sorszámú lap betöltése a memóriába.
     * @param pageNumber A betöltendő lap sorszáma, azonosítószáma.
     */
    public void loadPageIntoMemory(int pageNumber) {
        // a betöltendő lap kiválasztása pageNumber alapján
        Page toLoad = pages.get(pages.indexOf(new Page(pageNumber)));   // elvben pages.get(pageNumber) is elég lenne,
                                                                        // de így biztonságosabb
        toLoad.setDirty(false);
        toLoad.setRef(true);                            // a lapra hivatkoztak, ezért is töltjük be
        toLoad.setIsInMemory(true);

        physMem.add(toLoad);                            // a lapkeretek végéhez fűzzük
    }


    // Getterek

    public static VirtMemory getInstance() {
        if (instance == null) instance = new VirtMemory();
        return instance;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }


    // Setterek

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

}
