package ua.com.nov.model.domain.supertype;

import ua.com.nov.model.domain.PersistenceFacade;

import java.io.Serializable;

/**
 * This class is used to mark a proxy ('light').
 */
public class ProxyState extends PObjectState {
    private static ProxyState instance = new ProxyState();

    private ProxyState() {
    }

    public static ProxyState getInstance() {
        return instance;
    }

    /**
     * Change object's state to 'OldDelete'
     *
     * @param entity deleted object
     */
    @Override
    protected void delete(PersistentObject entity) {
        entity.setState(OldDeleteState.getInstance());
    }

    /**
     * Retrieve the 'full' vesion of given object from the persistent storage and change its state to 'OldClean'
     *
     * @param entity reloading object
     */
    @Override
    protected void retrieve(PersistentObject<? extends Serializable> entity) {
        PersistenceFacade.getInstance().retrieve(entity);
        entity.setState(OldCleanState.getInstance());
    }
}
