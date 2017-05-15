package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DaoSystemException;

public interface DataDefinitionDao<E> {
    // Create entity if not exist
    void createIfNotExist(E entity) throws DaoSystemException;

    //Delete entity with identifier = 'eId'
    void deleteIfExist(E entity) throws DaoSystemException;

    //Rename entity
    void rename(E entity, String newName) throws DaoSystemException;
}
