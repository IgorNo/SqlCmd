package ua.com.nov.model.dao;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public interface Dao<V> {

    Dao<V> setDataSource(DataSource dataSource);

    //Create
    void create(V value) throws SQLException;

    //Read one value from container
    V readOne(V value) throws SQLException;

    //Read N values from container
    List<V> readN(int nStart, int number, V template) throws SQLException;

    //Read All from container
    List<V> readAll(V template) throws SQLException;

    //Update
    void update(V value) throws SQLException;

    //Delete
    void delete(V value) throws SQLException;

    //Delete all from container
    void deleteAll(V template) throws SQLException;
    
    //Count
    int count(V template) throws SQLException;
}
