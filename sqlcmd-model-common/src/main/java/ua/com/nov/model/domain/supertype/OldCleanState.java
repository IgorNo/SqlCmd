package ua.com.nov.model.domain.supertype;

import java.io.Serializable;

/**
 * This class is used to mark an object retrieved from the persistence storage and not changed.
 */
public class OldCleanState extends PObjectState {
    private static OldCleanState instance = new OldCleanState();

    private OldCleanState() {
    }

    public static OldCleanState getInstance() {
        return instance;
    }

    /**
     * Change state the given object to 'OldDirty'
     *
     * @param entity changed object
     */
    @Override
    protected void save(PersistentObject<? extends Serializable> entity) {
        entity.setState(OldDirtyState.getInstance());
    }

    /**
     * Change state the given object to 'OldDeleted'
     *
     * @param entity deleted object
     */
    @Override
    protected void delete(PersistentObject<? extends Serializable> entity) {
        entity.setState(OldDeleteState.getInstance());
    }
}
