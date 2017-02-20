package ua.com.nov.model.dao;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<K, V, A> implements Dao<K, V, A> {

    private DataSource dataSource;

    protected DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void create(V value) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public V readByPK(K id) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<K, V> readAllFrom(A ambient) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<K, V> readAll() throws SQLException {
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
    public void deleteAllFrom(A ambient) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(A ambient) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
