package ua.com.nov.model.dao;

import javax.sql.DataSource;
import java.util.List;

public abstract class BaseDao<K, V> implements Dao<K, V> {

    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void create(V value) {
        throw new UnsupportedOperationException();
    }

    public V readByPK(K id) {
        throw new UnsupportedOperationException();
    }

    public List<V> readAll() {
        throw new UnsupportedOperationException();
    }

    public void update(V value) {
        throw new UnsupportedOperationException();
    }

    public void delete(V value) {
        throw new UnsupportedOperationException();
    }

    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        throw new UnsupportedOperationException();
    }
}
