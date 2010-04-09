package memsim_java;

/**
 * Laphiány-t reprezentáló osztály
 * @author zealot
 */
public class PageFaultException extends Exception {
    public PageFaultException(String msg) {
        super(msg);
        Main.pageFault++; //inkrementáljuk a pagefault-okat
    }
}
