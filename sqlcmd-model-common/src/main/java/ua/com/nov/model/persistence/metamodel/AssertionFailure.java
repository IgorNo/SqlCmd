package ua.com.nov.model.persistence.metamodel;

/**
 * Indicates failure of an assertion: a possible bug in meta model.
 *
 * @author Igor Novikov
 * @since 05.12.2017
 */
public class AssertionFailure extends RuntimeException {
    /**
     * Creates an instance of AssertionFailure using the given message.
     *
     * @param message The message explaining the reason for the exception
     */
    public AssertionFailure(String message) {
        super(message);
    }

    /**
     * Creates an instance of AssertionFailure using the given message and underlying cause.
     *
     * @param message The message explaining the reason for the exception
     * @param cause   The underlying cause.
     */
    public AssertionFailure(String message, Throwable cause) {
        super(message, cause);
    }
}
