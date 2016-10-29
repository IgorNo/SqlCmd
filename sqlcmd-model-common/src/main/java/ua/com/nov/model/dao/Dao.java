package ua.com.nov.model.dao;

import java.sql.SQLException;
import java.util.Map;

public interface Dao<K, V> {

    //Create
    void create(V value) throws SQLException;

    //Read by primary key
    V readByPK(K id) throws SQLException;

    //Read All
    Map<K, V> readAll() throws SQLException;

    //Update
    void update(V value) throws SQLException;

    //Delete
    void delete(V value) throws SQLException;

    //Delete
    void deleteAll() throws SQLException;

    //Count
    int count() throws SQLException;
}
