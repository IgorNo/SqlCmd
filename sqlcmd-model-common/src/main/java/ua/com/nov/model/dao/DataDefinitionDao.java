package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.MappingSystemException;

public interface DataDefinitionDao<E, C> {

    // Create entity if not exist
    void createIfNotExist(E entity) throws MappingSystemException;

    //Delete entity with identifier = 'eId'
    void deleteIfExist(E entity) throws MappingSystemException;

    //Rename entity
    default void rename(E entity, String newName) throws MappingSystemException {
        throw new UnsupportedOperationException();
    }
}
