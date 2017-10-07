package ua.com.nov.model.dao.exception;

public class DAOSystemException extends SystemException {
    public DAOSystemException(String message) {
        super(message);
    }

    public DAOSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
