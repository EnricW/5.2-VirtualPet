package s05.virtualpet.exception.custom;

public class PetAlreadyBankruptException extends RuntimeException {
    public PetAlreadyBankruptException(String message) {
        super(message);
    }
}
