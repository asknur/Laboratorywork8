package kg.attractor.labwork8.exception;

public class AlreadyCompletedException extends Exception {
    public AlreadyCompletedException(String s) {
        super("Already completed");
    }
}
