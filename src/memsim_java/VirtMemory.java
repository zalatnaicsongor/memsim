package memsim_java;

import java.util.*;
import virtmemory.algorithm.*;


/**
 * Virtuális memória osztály.
 *
 * @author Kádár István
 */
public class VirtMemory {

    /**
     * A lapok száma.
     */
    public final int NUMBEROFPAGES;
    /**
     * Az osztály egyedüli példánya
     */
    private static VirtMemory instance = null;

    /**
     * A lapok, mint a virtuális memória egységei.
     */
    public ArrayList<Page> pages;

    /**
     * A lepcserélő, ami megmondja, hogy melyik algoritmus alapján
     * történjen a lapcsere.
     */
    private PageReplaceStrategy pageReplacer;

    /**
     * Konstruktor, amely a lapokat létrehozza és inicializájla.
     */
    protected VirtMemory(PageReplaceStrategy prs, int pagenum, int pagesize) {

        this.pageReplacer = prs;
        this.NUMBEROFPAGES = pagenum;

        pages = new ArrayList<Page>(NUMBEROFPAGES);
        for (int i = 0; i < NUMBEROFPAGES; i++) {
            pages.add(new Page(i, pagesize));                     // lap létrehozása sorszámával inicializálva
        }
    }


    /**
     * Lap kidobás a memóriából.
     * @param out Az eldobandó lap.
     */
    public void throwOutPage(Page out) {
        out.setIsInMemory(false);                       // már nem lesz a mem-ben, most dobjuk ki
        if (out.getDirty()) {
            pages.set(out.getPageNumber(), out);        // ha modosított, visszaírjuk a helyére
        }
        Memory.getInstance().getPageFrames()
                .remove(out);                           // törlés a memóriából

        System.out.println(out.getPageNumber() + " számú lap kidobva...");
    }

    /**
     * Adott sorszámú lap betöltése a memóriába.
     * @param pageNumber A betöltendő lap sorszáma, azonosítószáma.
     */
    public void loadPageIntoMemory(int pageNumber) {
        // a betöltendő lap kiválasztása pageNumber alapján, elveben pages.get(pagenumber) is elég lenne
        int pagesize = Memory.getInstance().PAGESIZE;
        Page toLoad = pages.get(pages.indexOf(new Page(pageNumber, pagesize)));
        toLoad.setDirty(false);
        toLoad.setRef(true);                            // a lapra hivatkoztak, ezért is töltjük be
        toLoad.setIsInMemory(true);
        toLoad.setCounter(0);
        Memory.getInstance().getPageFrames().
                addLast(toLoad);                        // a lapkeretek végéhez fűzzük
        
        System.out.println(toLoad.getPageNumber() + " számú lap betöltve...");
    }


    // Getterek

    public static VirtMemory getInstance() {
        return VirtMemory.instance;
    }

    public static void createVirtMemory(PageReplaceStrategy prs, int pagenum, int pagesize) {
        VirtMemory.instance = new VirtMemory(prs, pagenum, pagesize);
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public PageReplaceStrategy getPageReplacer() {
        return pageReplacer;
    }


    // Setterek

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public void setPageReplacer(PageReplaceStrategy pageReplacer) {
        this.pageReplacer = pageReplacer;
    }

}
