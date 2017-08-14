package ua.com.nov.model.dao;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.GenericTable;
import ua.com.nov.model.entity.metadata.table.Table;

import java.util.List;

public interface DataManipulationDao<R extends AbstractRow<R>> {

    // Insert row into table and get autoincrement key
    KeyHolder insert(R row) throws MappingSystemException;

    //Read N rows from container starting with nStart position
    List<R> readN(GenericTable<R> table, long nStart, int number) throws MappingSystemException;

    List<R> readAll(GenericTable<R> table) throws MappingSystemException;

    // Read filtered entities
    List<R> readFetch(GenericTable<R> table, FetchParameter... parameters) throws MappingSystemException;

    //Delete values all from container
    void deleteAll(Table table) throws MappingSystemException;

    //Count number of values in container
    int count(Table table) throws MappingSystemException;
}
