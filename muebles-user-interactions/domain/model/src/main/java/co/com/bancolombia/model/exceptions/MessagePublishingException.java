package co.com.bancolombia.model.exceptions;

public class MessagePublishingException extends RuntimeException {

    public MessagePublishingException(String message) {
        super(message);
    }

    public MessagePublishingException(String message, Throwable cause) {
        super(message, cause);
    }
}
