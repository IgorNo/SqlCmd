package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DaoSystemException;

public interface DataDefinitionDao<I,E> {
    // Create entity if not exist
    void createIfNotExist(E entity) throws DaoSystemException;

    //Delete entity with identifier = 'eId'
    void deleteIfExist(I eId) throws DaoSystemException;

    //Rename entity
    void rename(I eId, String newName) throws DaoSystemException;
}
