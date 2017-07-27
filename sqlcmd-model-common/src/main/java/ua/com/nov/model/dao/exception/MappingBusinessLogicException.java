package ua.com.nov.model.dao.exception;

public class MappingBusinessLogicException extends RuntimeException {

    public MappingBusinessLogicException(String message) {
        super(message);
    }

    public MappingBusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
