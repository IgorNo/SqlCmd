package ua.com.nov.model.dao;

import java.util.List;

public interface Dao<K, V> {

    //Create
    void create(V value) throws SQLExc;

    //Read by primary key
    V readByPK(K id);

    //Read All
    List<V> readAll();

    //Update
    void update(V value);

    //Delete
    void delete(V value);

    //Delete
    void deleteAll();

    //Count
    int count();
}
