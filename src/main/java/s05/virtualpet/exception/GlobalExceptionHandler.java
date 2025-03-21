package s05.virtualpet.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import s05.virtualpet.exception.custom.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        log.error("Invalid credentials attempt: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Credentials", ex.getMessage());
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJwt(InvalidJwtTokenException ex) {
        log.error("Invalid or expired JWT token: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameExists(UsernameAlreadyExistsException ex) {
        log.warn("Username already exists: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Username Already Exists", ex.getMessage());
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePetNotFound(PetNotFoundException ex) {
        log.error("Pet not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Pet Not Found", ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedPetAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedPet(UnauthorizedPetAccessException ex) {
        log.warn("Unauthorized pet access attempt: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Unauthorized Pet Access", ex.getMessage());
    }

    @ExceptionHandler(InvalidPetActionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAction(InvalidPetActionException ex) {
        log.error("Invalid pet action: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Pet Action", ex.getMessage());
    }

    @ExceptionHandler(PetOutOfChipsException.class)
    public ResponseEntity<ErrorResponse> handlePetOutOfChips(PetOutOfChipsException ex) {
        log.warn("Pet out of chips: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Pet Out of Chips", ex.getMessage());
    }

    @ExceptionHandler(PetAlreadyBankruptException.class)
    public ResponseEntity<ErrorResponse> handleBankruptPet(PetAlreadyBankruptException ex) {
        log.warn("Pet is already bankrupt: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Pet Already Bankrupt", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status, error, message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
