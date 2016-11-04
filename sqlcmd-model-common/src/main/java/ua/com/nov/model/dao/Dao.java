package ua.com.nov.model.dao;

import java.sql.SQLException;
import java.util.Map;

public interface Dao<K, V, A> {

    //Create
    void create(V value) throws SQLException;

    //Read by primary key
    V readByPK(K id) throws SQLException;

    //Read All from ambient
    Map<K, V> readAllFrom(A ambient) throws SQLException;

    //Read All
    Map<K, V> readAll() throws SQLException;

    //Update
    void update(V value) throws SQLException;

    //Delete
    void delete(V value) throws SQLException;

    //Delete all from ambient
    void deleteAllFrom(A ambient) throws SQLException;
    
    //Count
    int count(A ambient) throws SQLException;
}
