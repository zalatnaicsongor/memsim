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
     * A lepcserélő, ami megmondja, hogy melyik algoritmus alapján
     * történjen a lapcsere.
     */
    private PageReplaceStrategy pageReplacer = PageReplaceFIFO.getInstance();

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
        out.setIsInMemory(false);                       // már nem lesz a mem-ben, most dobjuk ki
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
        Page toLoad = pages.get(pages.indexOf(new Page(pageNumber)));
        //ez így BIZTOS nem jó, az equals csak akkor true, ha ugyanarra hivatkozik
        //és mivel itt NEW, ezért az nem ugyanaz, see API
        /* Page toLoad = null;
         * Iterator<Page> it = pages.iterator();
         * while (it.hasNext()) {
         *    Page tempPage = it.next();
         *    if (tempPage.getPageNumber() == pageNumber) {
         *        toLoad = tempPage;
         *        break;
         *    }
         * }
         *
         * Az ugyanarra hivatkozás az a '==' operátor, az equalst pedig személyesn
         * irtam meg a Page osztályban:
         *
            ** Két lap akkor ugyanaz, ha a sorszámuk (pageNumber) megegyezik.
             * @param page A hasonlító lap.
             * @return true ha megegyeznek.
             *
            public boolean equals(Page page) {
                return pageNumber == page.getPageNumber();
            }
         *
         * http://leepoint.net/notes-java/data/expressions/22compareobjects.html
         */



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
