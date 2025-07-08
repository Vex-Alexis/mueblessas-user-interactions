package co.com.bancolombia.model.exceptions;

public class InvalidHashException extends RuntimeException {
    public InvalidHashException(String message) {
        super(message);
    }
}
