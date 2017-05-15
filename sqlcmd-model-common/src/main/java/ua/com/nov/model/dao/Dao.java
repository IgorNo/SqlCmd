package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;

import javax.sql.DataSource;
import java.util.List;

public interface Dao<I, E, C> {

    DataSource getDataSource();

    Dao<I, E, C> setDataSource(DataSource dataSource);

    // Create entity
    void create(E entity) throws DaoSystemException;

    // Update entity
    void update(E entity) throws DaoSystemException;

    //Delete entity with identifier = 'eId'
    void delete(E entity) throws DaoSystemException;

    // Read one entity with identifier = 'eId'
    E read(I eId) throws DaoSystemException, DaoBusinessLogicException;

    //Read All from container with identifier = 'cId'
    List<E> readAll(C cId) throws DaoSystemException;
}
