package ua.com.nov.model.domain;

import org.springframework.data.domain.Persistable;
import ua.com.nov.model.domain.factory.RepositoryFactory;

import java.io.Serializable;

/**
 * Specifies an external interface for interacting with persistent storage.
 *
 * @author Igor Novikov
 * @since 01.11.2017
 */
public final class PersistenceFacade {
    private static PersistenceFacade instance;

    private RepositoryFactory factory;

    private PersistenceFacade(RepositoryFactory factory) {
        this.factory = factory;
    }

    public static PersistenceFacade createInstance(RepositoryFactory factory) {
        if (instance != null) {
            throw new IllegalStateException("PersistenceFacade already created - use getInstance()");
        }
        instance = new PersistenceFacade(factory);
        return instance;
    }

    public static PersistenceFacade getInstance() {
        if (instance == null)
            throw new IllegalStateException("PersistenceFacade not created - use createInstance(RepositoryFactory factory)");
        return instance;
    }

    @SuppressWarnings("unchecked")
    public void save(Persistable<? extends Serializable> entity) {
        factory.getRepository(entity.getClass()).save(entity);
    }

    @SuppressWarnings("unchecked")
    public void delete(Persistable<? extends Serializable> entity) {
        factory.getRepository(entity.getClass()).delete(entity);
    }

    @SuppressWarnings("unchecked")
    public void reload(Persistable<? extends Serializable> entity) {
        factory.getRepository(entity.getClass()).reload(entity);
    }

    @SuppressWarnings("unchecked")
    public void retrieve(Persistable<? extends Serializable> entity) {
        factory.getRepository(entity.getClass()).retrieve(entity);
    }

}
