package memsim_java;

/**
 * szabálytalan pointer-műveletekkor dobott kivétel
 * @author zalatnaicsongor
 */
public class PointerOutOfRangeException extends Exception {
    public PointerOutOfRangeException(String Message) {
        super(Message);
    }
}
