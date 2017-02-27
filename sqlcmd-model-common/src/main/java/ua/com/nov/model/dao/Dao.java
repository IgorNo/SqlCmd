package ua.com.nov.model.dao;

import ua.com.nov.model.entity.Persistent;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public interface Dao<K,V> {

    Dao<K,V> setDataSource(DataSource dataSource);

    //Create
    void create(V value) throws SQLException;

    //Read one value from container
    V read(K key) throws SQLException;

    //Read N values from container
    List<V> readN(int nStart, int number, K template) throws SQLException;

    //Read All from container
    List<V> readAll(K template) throws SQLException;

    //Update
    void update(V value) throws SQLException;

    //Delete
    void delete(K key) throws SQLException;

    //Delete all from container
    void deleteAll(K key) throws SQLException;
    
    //Count
    int count(K key) throws SQLException;
}
