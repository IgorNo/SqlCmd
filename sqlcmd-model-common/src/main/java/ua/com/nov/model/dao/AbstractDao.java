package ua.com.nov.model.dao;

import ua.com.nov.model.entity.Mappable;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<K extends Persistent<V>, V extends Unique> implements Dao<K, V> {

    private DataSource dataSource;

    protected DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public AbstractDao<K, V> setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    protected abstract void executeUpdateStmt(String createStmt) throws SQLException;

    protected abstract ResultSet getOneResultSet(K key) throws SQLException;

    protected abstract ResultSet getAllResultSet(K template) throws SQLException;

    protected abstract ResultSet getNResultSet(int nStart, int number, K template) throws SQLException;

    @Override
    public void create(V value) throws SQLException {
        executeUpdateStmt(value.getId().getSqlStmtSource().getCreateStmt(value));
    }

    @Override
    public V read(K key) throws SQLException {
        ResultSet rs = getOneResultSet(key);
        Mappable<V> rowMapper = key.getRowMapper();
        return rowMapper.rowMap(rs);
    }

    @Override
    public List<V> readN(int nStart, int number, K template) throws SQLException {
        ResultSet rs = getNResultSet(nStart, number, template);
        return getList(rs, template);
    }

    @Override
    public List<V> readAll(K template) throws SQLException {
        ResultSet rs = getAllResultSet(template);
        return getList(rs, template);
    }

    private List<V> getList(ResultSet rs, K template) throws SQLException {
        List<V> result = new ArrayList();
        while (rs.next()) {
            Mappable<V> rowMapper = template.getRowMapper();
            result.add(rowMapper.rowMap(rs));
        }
        if (rs.getStatement() != null && !rs.getStatement().isClosed()) rs.getStatement().close();
        return result;
    }

    @Override
    public void update(V value) throws SQLException {
        executeUpdateStmt(value.getId().getSqlStmtSource().getUpdateStmt(value));
    }

    @Override
    public void delete(K key) throws SQLException {
        executeUpdateStmt(key.getSqlStmtSource().getDeleteStmt(key));
    }

    @Override
    public void deleteAll(K template) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(K template) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
