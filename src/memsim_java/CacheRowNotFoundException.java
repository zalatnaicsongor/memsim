package memsim_java;

/**
 * row-hiánykor dobott kivétel
 * @author zalatnaicsongor
 */
public class CacheRowNotFoundException extends Exception {
    /**
     * a konstruktor az ősnek továbbadja az üzenetet
     * @param Message
     */
    public CacheRowNotFoundException(String Message) {
        super(Message);
    }
}