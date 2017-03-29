package ua.com.nov.model.dao;

import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.statement.SqlStatement;
import ua.com.nov.model.statement.SqlStatementSource;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<K extends MetaDataId<C>, V extends Unique<K>, C extends Persistent> implements Dao<K,V,C> {

    private DataSource dataSource;

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public AbstractDao<K,V,C> setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public void create(V value) throws SQLException {
        executeUpdateStmt(getSqlStmtSource(value.getId().getDb()).getCreateStmt(value));
    }

    @Override
    public void update(V value) throws SQLException {
        executeUpdateStmt(getSqlStmtSource(value.getId().getDb()).getUpdateStmt(value));
    }

    @Override
    public void delete(K key) throws SQLException {
        executeUpdateStmt(getSqlStmtSource(key.getDb()).getDeleteStmt(key));
    }

    protected abstract void executeUpdateStmt(SqlStatement stmt) throws SQLException;

    protected abstract SqlStatementSource<K,V,C> getSqlStmtSource(Database db);

    @Override
    public V read(K key) throws SQLException {
        ResultSet rs = getResultSet(key);
        return rowMap(key.getContainerId(), checkResultSet(rs, key));
    }

    // get one item ResultSet
    protected ResultSet getResultSet(K key) throws SQLException {
        throw new UnsupportedOperationException();
    }

    protected abstract V rowMap(C containerId, ResultSet rs) throws SQLException;

    private ResultSet checkResultSet(ResultSet rs, K id) throws SQLException {
        if (!rs.next()) throw new IllegalArgumentException(String.format("%s '%s' doesn't exist in %s '%s'.",
                id.getClass().getSimpleName(), id.getName(),
                id.getContainerId().getFullName(), id.getContainerId().getClass().getSimpleName()));
        return rs;
    }

    @Override
    public List<V> readN(int nStart, int number, C container) throws SQLException {
        ResultSet rs = getResultSetN(nStart, number, container);
        return getList(rs, container);
    }

    // get N items ResultSet
    protected ResultSet getResultSetN(int nStart, int number, C containerId) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<V> readAll(C containerId) throws SQLException {
        ResultSet rs = getResultSetAll(containerId);
        return getList(rs, containerId);
    }

    // get all items ResultSet
    protected ResultSet getResultSetAll(C container) throws SQLException {
        throw new UnsupportedOperationException();
    }

    private List<V> getList(ResultSet rs, C containerId) throws SQLException {
        List<V> result = new ArrayList();
        while (rs.next()) {
            result.add(rowMap(containerId, rs));
        }
        if (rs.getStatement() != null && !rs.getStatement().isClosed()) rs.getStatement().close();
        return result;
    }

    @Override
    public void deleteAll(C containerId) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(C containerId) throws SQLException {
        throw new UnsupportedOperationException();
    }

    protected DatabaseMetaData getDbMetaData() throws SQLException {
        return getDataSource().getConnection().getMetaData();
    }

}
