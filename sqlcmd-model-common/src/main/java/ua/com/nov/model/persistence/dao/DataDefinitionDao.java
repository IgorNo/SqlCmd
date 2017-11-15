package ua.com.nov.model.persistence.dao;


import ua.com.nov.model.persistence.dao.exception.NoSuchEntityException;

/**
 * Interface for generic CRUD operations on metadata.
 *
 * @param <E>  - metadata entity
 * @param <ID> - metadata entity identifier
 * @param <C>  - metadata container identifier (higher level metadata)
 * @author Igor Novikov
 */
public interface DataDefinitionDao<E, ID, C> {
    /**
     * Creates given metadata entity.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void create(E entity);

    /**
     * Creates given metadata entity if the one not exists.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void createIfNotExist(E entity);

    /**
     * Saves content of given metadata entity.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void alter(E entity);

    /**
     * Renames entity {@code entity} to name {@code newName}.
     *
     * @param entity must not be {@literal null}.
     * @param newName must not be {@literal null}.
     * @throws IllegalArgumentException if {@code entity} is {@literal null} or {@code newName} is {@literal null}.
     */
    default void rename(E entity, String newName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
     */
    void drop(ID id);

    /**
     * Deletes the entity with the given id if the one exist.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void dropIfExist(ID id);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id
     * @throws NoSuchEntityException    if the entity with the given id was not found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    E read(ID id);

    /**
     * Returns all metadata instances of the given container.
     *
     * @return all entities
     */
    Iterable<E> readAll(C cId);
}
