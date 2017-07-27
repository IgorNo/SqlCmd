package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.MappingSystemException;

import javax.sql.DataSource;
import java.util.List;

public interface Dao<I, E, C> {

    DataSource getDataSource();

    void setDataSource(DataSource dataSource);

    // Create entity
    void create(E entity) throws MappingSystemException;

    // Update entity
    void update(E entity) throws MappingSystemException;

    //Delete entity with identifier = 'eId'
    void delete(E entity) throws MappingSystemException;

    // Read one entity with identifier = 'eId'
    E read(I eId) throws MappingSystemException;

    //Read All from container
    List<E> readAll(C cId) throws MappingSystemException;
}
