package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DAOSystemException;

import javax.sql.DataSource;
import java.util.List;

public interface Dao<I, E, C> {

    DataSource getDataSource();

    void setDataSource(DataSource dataSource);

    // Create entity
    void create(E entity) throws DAOSystemException;

    // Update entity
    void update(E entity) throws DAOSystemException;

    //Delete entity with identifier = 'eId'
    void delete(I eId) throws DAOSystemException;

    // Read one entity with identifier = 'eId'
    E read(I eId) throws DAOSystemException;

    //Read All from container
    List<E> readAll(C cId) throws DAOSystemException;
}
