package ua.com.nov.model.dao;

import java.sql.SQLException;
import java.util.Map;

public abstract class BaseDao<K, V, O> implements Dao<K, V, O> {

    @Override
    public void create(V value) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public V readOne(K id) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<K, V> readN(int nStart, int number) throws SQLException {
        return null;
    }

    @Override
    public Map<K, V> readAll(O topLevelObject) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(V value) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(V value) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(O topLevelObject) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(O topLevelObject) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
