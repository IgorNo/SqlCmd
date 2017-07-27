package ua.com.nov.model.dao.exception;

public class MappingSystemException extends MappingException {
    public MappingSystemException(String message) {
        super(message);
    }

    public MappingSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
