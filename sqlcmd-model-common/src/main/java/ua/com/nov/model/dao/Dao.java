package ua.com.nov.model.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<K, V> {

    //Create
    void create(V value) throws SQLException;

    //Read by primary key
    V readByPK(K id) throws SQLException;

    //Read All
    List<V> readAll() throws SQLException;

    //Update
    void update(V value) throws SQLException;

    //Delete
    void delete(V value) throws SQLException;

    //Delete
    void deleteAll() throws SQLException;

    //Count
    int count() throws SQLException;
}
