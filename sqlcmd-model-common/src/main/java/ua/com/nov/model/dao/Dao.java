package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DaoSystemException;

import javax.sql.DataSource;

public interface Dao<I, E> {

    DataSource getDataSource();

    void setDataSource(DataSource dataSource);

    // Create entity
    void create(E entity) throws DaoSystemException;

    // Update entity
    void update(E entity) throws DaoSystemException;

    //Delete entity with identifier = 'eId'
    void delete(E entity) throws DaoSystemException;

    // Read one entity with identifier = 'eId'
    E read(I eId) throws DaoSystemException;

}
