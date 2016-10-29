package ua.com.nov.model.dao;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<K, V> implements Dao<K, V> {

    private DataSource dataSource;

    public DataSource getDataSource() {
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
    public void deleteAll() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count() throws SQLException {
        throw new UnsupportedOperationException();
    }
}
