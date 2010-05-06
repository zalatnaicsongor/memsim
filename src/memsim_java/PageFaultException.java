package memsim_java;

/**
 * Laphiány-t reprezentáló osztály
 * @author zealot
 */
public class PageFaultException extends Exception {
    public PageFaultException(String msg) {
        super(msg);
        Statistics.getInstance().addPageFault(); //inkrementáljuk a pagefault-okat
    }
}
