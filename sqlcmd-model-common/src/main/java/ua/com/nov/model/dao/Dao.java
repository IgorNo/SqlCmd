package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DaoBusinesException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.fetch.FetchParametersSource;

import javax.sql.DataSource;
import java.util.List;

public interface Dao<K,V,C> {

    DataSource getDataSource();

    Dao<K,V,C> setDataSource(DataSource dataSource);

    //Create
    void create(V value) throws DaoSystemException;

    //Read one value from container
    V read(K key) throws DaoSystemException, DaoBusinesException;

    // Read fetching values
    <T extends FetchParametersSource<C>> List<V> readFetch(T parameters) throws DaoSystemException;

    //Read All from container
    List<V> readAll(C containerId) throws DaoSystemException;

    //Update
    void update(V value) throws DaoSystemException;

    //Delete
    void delete(K key) throws DaoSystemException;

}
