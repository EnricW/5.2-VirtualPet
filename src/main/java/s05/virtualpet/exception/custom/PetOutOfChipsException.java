package s05.virtualpet.exception.custom;

public class PetOutOfChipsException extends RuntimeException {
    public PetOutOfChipsException(String message) {
        super(message);
    }
}
