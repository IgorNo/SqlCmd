package ua.com.nov.model.dao.exception;

public class NoSuchEntityException extends DaoBusinessLogicException {
    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}