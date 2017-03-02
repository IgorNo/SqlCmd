package ua.com.nov.model.dao;

import ua.com.nov.model.entity.Child;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.statement.SqlStatementSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<K extends Persistent & Child<C>, V extends Unique<K>, C> implements Dao<K,V,C> {

    private DataSource dataSource;

    protected DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public AbstractDao<K,V,C> setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    protected abstract void executeUpdateStmt(String createStmt) throws SQLException;

    protected abstract ResultSet getOneResultSet(K key) throws SQLException;

    protected abstract ResultSet getAllResultSet(C container) throws SQLException;

    protected abstract ResultSet getNResultSet(int nStart, int number, C containerId) throws SQLException;

    protected abstract V rowMap(C containerId, ResultSet rs) throws SQLException;

    protected abstract SqlStatementSource<K,V,C> getSqlStmtSource(Database db);

    @Override
    public void create(V value) throws SQLException {
        executeUpdateStmt(getSqlStmtSource(value.getId().getDb()).getCreateStmt(value));
    }

    @Override
    public V read(K key) throws SQLException {
        ResultSet rs = getOneResultSet(key);
        return rowMap(key.getContainerId(), rs);
    }

    @Override
    public List<V> readN(int nStart, int number, C container) throws SQLException {
        ResultSet rs = getNResultSet(nStart, number, container);
        return getList(rs, container);
    }

    @Override
    public List<V> readAll(C containerId) throws SQLException {
        ResultSet rs = getAllResultSet(containerId);
        return getList(rs, containerId);
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
    public void update(V value) throws SQLException {
        executeUpdateStmt(getSqlStmtSource(value.getId().getDb()).getUpdateStmt(value));
    }

    @Override
    public void delete(K key) throws SQLException {
        executeUpdateStmt(getSqlStmtSource(key.getDb()).getDeleteStmt(key));
    }

    @Override
    public void deleteAll(C containerId) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(C containerId) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
