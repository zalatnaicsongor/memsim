package memsim_java;

/**
 * helyhiányt reprezentáló kivétel
 * @author zalatnaicsongor
 */
public class MemorySpaceException extends Exception {
    public MemorySpaceException(String Message) {
        super(Message);
    }
}
