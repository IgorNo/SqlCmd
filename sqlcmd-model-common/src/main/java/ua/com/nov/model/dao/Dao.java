package ua.com.nov.model.dao;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public interface Dao<K,V,C> {

    Dao<K,V,C> setDataSource(DataSource dataSource);

    //Create
    void create(V value) throws SQLException;

    //Read one value from container
    V read(K key) throws SQLException;

    //Read N values from container
    List<V> readN(int nStart, int number, C containerId) throws SQLException;

    //Read All from container
    List<V> readAll(C containerId) throws SQLException;

    //Update
    void update(V value) throws SQLException;

    //Delete
    void delete(K key) throws SQLException;

    //Delete all from container
    void deleteAll(C containerId) throws SQLException;
    
    //Count
    int count(C containerId) throws SQLException;
}
