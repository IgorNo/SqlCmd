package ua.com.nov.model.dao.exception;

public class DaoBusinessLogicException extends RuntimeException {

    public DaoBusinessLogicException(String message) {
        super(message);
    }

    public DaoBusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
