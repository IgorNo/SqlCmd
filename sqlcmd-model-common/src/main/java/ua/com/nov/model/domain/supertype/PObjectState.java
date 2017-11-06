package ua.com.nov.model.domain.supertype;

import java.io.Serializable;

/**
 * <p>The base class to which the {@link PersistentObject} object delegates behavior that makes it persistent.</p>
 * <p>
 * <p>By default, all operations are not performed.</p>
 *
 * @author Igor Novikov
 * @since 01.11.2017
 */
public abstract class PObjectState {

    /**
     * Fix all changes of the given persistence object into persistence storage
     *
     * @param entity committed object
     */
    protected void commit(PersistentObject<? extends Serializable> entity) {
    }

    /**
     * Refresh all attributes of the given object from persistence storage
     *
     * @param entity reloading object
     */
    protected void rollback(PersistentObject<? extends Serializable> entity) {
    }

    /**
     * Mark the given object for deletion
     *
     * @param entity deleted object
     */
    protected void delete(PersistentObject<? extends Serializable> entity) {
    }

    /**
     * Retrieve the full version of the given object from persistence storage
     *
     * @param entity retrieving object
     */
    protected void retrieve(PersistentObject<? extends Serializable> entity) {
    }

    /**
     * Mark the given object for saving
     *
     * @param entity changed object
     */
    protected void save(PersistentObject<? extends Serializable> entity) {
    }
}
