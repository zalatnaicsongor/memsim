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
     * Konstruktor, amely a lapokat létrehozza és inicializájla.
     */
    protected VirtMemory() {
        pages = new ArrayList<Page>(NUMBEROFPAGES);
        for (int i = 0; i < NUMBEROFPAGES; i++) {
            pages.add(new Page(i));                     // lap létrehozása sorszámával inicializálva
        }
    }


    // Getterek

    public static VirtMemory getInstance() {
        if (instance == null) instance = new VirtMemory();
        return instance;
    }


    // Setterek

}
