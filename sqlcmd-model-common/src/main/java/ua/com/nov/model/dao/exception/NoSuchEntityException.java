package ua.com.nov.model.dao.exception;

public class NoSuchEntityException extends DaoBusinesException {
    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
