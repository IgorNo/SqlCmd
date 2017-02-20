package ua.com.nov.model.dao;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

public interface Dao<K, V, O> {

    void setDataSource(DataSource dataSource);

    //Create
    void create(V value) throws SQLException;

    //Read by primary key
    V readOne(K id) throws SQLException;

    //Read N values
    Map<K, V> readN(int nStart, int number) throws SQLException;

    //Read All from top level Object
    Map<K, V> readAll(O topLevelObject) throws SQLException;

    //Update
    void update(V value) throws SQLException;

    //Delete
    void delete(V value) throws SQLException;

    //Delete all from ambient
    void deleteAll(O topLevelObject) throws SQLException;
    
    //Count
    int count(O topLevelObject) throws SQLException;
}
