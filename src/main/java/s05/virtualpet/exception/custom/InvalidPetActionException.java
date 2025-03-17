package s05.virtualpet.exception.custom;

public class InvalidPetActionException extends RuntimeException {
    public InvalidPetActionException(String message) {
        super(message);
    }
}
