package ua.com.nov.model.domain.factory;

import org.springframework.data.domain.Persistable;
import ua.com.nov.model.persistence.repository.PersistenceRepository;

import java.io.Serializable;

/**
 * Interface for repository factory
 * Provides instances of repositories for a given entity.
 *
 * @author Igor Novikov
 * @since 01.11.2017
 */
public interface RepositoryFactory {

    <T extends Persistable<ID>, ID extends Serializable>
    PersistenceRepository<T, ID> getRepository(Class<T> repositoryClass);
}
