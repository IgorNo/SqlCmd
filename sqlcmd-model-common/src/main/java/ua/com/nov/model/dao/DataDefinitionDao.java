package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DaoSystemException;

import java.util.List;

public interface DataDefinitionDao<E, C> {
    //Read All from container
    List<E> readAll(C cId) throws DaoSystemException;

    // Create entity if not exist
    void createIfNotExist(E entity) throws DaoSystemException;

    //Delete entity with identifier = 'eId'
    void deleteIfExist(E entity) throws DaoSystemException;

    //Rename entity
    default void rename(E entity, String newName) throws DaoSystemException {
        throw new UnsupportedOperationException();
    }
}
