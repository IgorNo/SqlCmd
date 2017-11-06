package ua.com.nov.model.domain.supertype;

import ua.com.nov.model.domain.PersistenceFacade;

import java.io.Serializable;

/**
 *
 */
public class OldDeleteState extends PObjectState {
    private static OldDeleteState instance = new OldDeleteState();

    private OldDeleteState() {
    }

    public static OldDeleteState getInstance() {
        return instance;
    }

    @Override
    protected void commit(PersistentObject<? extends Serializable> entity) {
        PersistenceFacade.getInstance().delete(entity);
        entity.setState(DeletedState.getInstance());
    }

    @Override
    protected void rollback(PersistentObject<? extends Serializable> entity) {
        PersistenceFacade.getInstance().reload(entity);
        entity.setState(OldCleanState.getInstance());
    }
}
