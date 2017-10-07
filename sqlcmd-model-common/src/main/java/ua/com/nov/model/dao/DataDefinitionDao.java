package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DAOSystemException;

public interface DataDefinitionDao<I, E> {

    // Create entity if not exist
    void createIfNotExist(E entity) throws DAOSystemException;

    //Delete entity with identifier = 'eId'
    void deleteIfExist(I eId) throws DAOSystemException;

    //Rename entity
    default void rename(E entity, String newName) throws DAOSystemException {
        throw new UnsupportedOperationException();
    }
}
