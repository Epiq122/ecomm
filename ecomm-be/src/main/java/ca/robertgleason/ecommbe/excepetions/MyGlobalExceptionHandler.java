package ca.robertgleason.ecommbe.excepetions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the e-commerce application.
 * <p>
 * Exception handling best practices:
 * 1. Centralize exception handling with @RestControllerAdvice
 * 2. Create specific handlers for different exception types
 * 3. Return appropriate HTTP status codes with error messages
 */
@RestControllerAdvice
public class MyGlobalExceptionHandler {

    /**
     * Handles validation exceptions thrown by @Valid annotation.
     * Extracts field names and error messages for clear client feedback.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            // you can customize the error message if needed
            String errorMessage = error.getDefaultMessage();
            response.put(fieldName, errorMessage);
        });
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

}
