package ua.com.nov.model.dao;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.metadata.table.Table;

import java.util.List;

public interface DataManipulationDao<E> {

    // Insert value into table and get autoincrement key
    KeyHolder insert(E entity) throws MappingSystemException;

    //Read N rows from container starting with nStart position
    List<E> readN(Table table, long nStart, int number) throws MappingSystemException;

    List<E> readAll(Table table) throws MappingSystemException;

    // Read filtered entities
    List<E> readFetch(Table table, FetchParameter... parameters) throws MappingSystemException;

    //Delete values all from container
    void deleteAll(Table table) throws MappingSystemException;

    //Count number of values in container
    int count(Table table) throws MappingSystemException;
}
