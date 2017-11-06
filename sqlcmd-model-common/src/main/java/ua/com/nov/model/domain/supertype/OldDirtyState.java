package ua.com.nov.model.domain.supertype;

import ua.com.nov.model.domain.PersistenceFacade;

import java.io.Serializable;

/**
 * This class is used to mark retrieved from the persistence storage and then changed objects.
 */
public class OldDirtyState extends PObjectState {
    private static OldDirtyState instance = new OldDirtyState();

    private OldDirtyState() {
    }

    public static OldDirtyState getInstance() {
        return instance;
    }

    /**
     * Update the given object in the persistent storage and change its state to 'OldClean'
     *
     * @param entity updated object
     */
    @Override
    protected void commit(PersistentObject<? extends Serializable> entity) {
        PersistenceFacade.getInstance().save(entity);
        entity.setState(OldCleanState.getInstance());
    }

    /**
     * Reload the given object from the persistent storage and change its state to 'OldClean'
     *
     * @param entity reloading object
     */
    @Override
    protected void rollback(PersistentObject<? extends Serializable> entity) {
        PersistenceFacade.getInstance().reload(entity);
        entity.setState(OldCleanState.getInstance());
    }

    /**
     * Change object's state to 'OldDelete'
     *
     * @param entity deleted object
     */
    @Override
    protected void delete(PersistentObject<? extends Serializable> entity) {
        entity.setState(OldDeleteState.getInstance());
    }
}
