package ua.com.nov.model.domain.supertype;

import ua.com.nov.model.domain.PersistenceFacade;

import java.io.Serializable;

/**
 * This class is used to mark a newly created object and not retrieved from the persistence storage.
 */
public class NewState extends PObjectState {
    private static final NewState instance = new NewState();

    private NewState() {
    }

    public static NewState getInstance() {
        return instance;
    }

    /**
     * Persist the given object and change its state to 'OldClean'
     *
     * @param entity committed object
     */
    @Override
    protected void commit(PersistentObject<? extends Serializable> entity) {
        PersistenceFacade.getInstance().save(entity);
        entity.setState(OldCleanState.getInstance());
    }

    /**
     * Change state the given object to 'Deleted'
     *
     * @param entity deleted object
     */
    @Override
    protected void delete(PersistentObject<? extends Serializable> entity) {
        entity.setState(DeletedState.getInstance());
    }
}
