package kg.attractor.labwork8.exception;

import java.util.NoSuchElementException;

public class NotFoundException extends NoSuchElementException {
    public NotFoundException() {
        super("Not found");
    }
}
