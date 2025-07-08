package co.com.bancolombia.model.exceptions;

public class DataPersistenceException extends RuntimeException {
    public DataPersistenceException(String message) {
        super(message);
    }
    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
