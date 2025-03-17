package s05.virtualpet.exception.custom;

public class UnauthorizedPetAccessException extends RuntimeException {
    public UnauthorizedPetAccessException(String message) {
        super(message);
    }
}
