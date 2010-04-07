package memsim_java;

/**
 * Laphiány-t reprezentáló osztály
 * @author zealot
 */
public class PageFault extends Exception {
    public PageFault(String msg) {
        super(msg);
    }
}
