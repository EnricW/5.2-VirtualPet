package s05.virtualpet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import s05.virtualpet.exception.custom.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Credentials", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameExists(UsernameAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Username Already Exists", ex.getMessage());
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePetNotFound(PetNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Pet Not Found", ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedPetAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedPet(UnauthorizedPetAccessException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Unauthorized Pet Access", ex.getMessage());
    }

    @ExceptionHandler(InvalidPetActionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAction(InvalidPetActionException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Pet Action", ex.getMessage());
    }

    @ExceptionHandler(PetOutOfChipsException.class)
    public ResponseEntity<ErrorResponse> handlePetOutOfChips(PetOutOfChipsException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Pet Out of Chips", ex.getMessage());
    }

    @ExceptionHandler(PetAlreadyBankruptException.class)
    public ResponseEntity<ErrorResponse> handleBankruptPet(PetAlreadyBankruptException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Pet Already Bankrupt", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status, error, message));
    }
}
