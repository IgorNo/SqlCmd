package ua.com.nov.model.dao;

import ua.com.nov.model.entity.Mappable;
import ua.com.nov.model.entity.Persistent;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<V extends Persistent> implements Dao<V> {

    private DataSource dataSource;

    protected DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public AbstractDao<V> setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    protected abstract void executeUpdateStmt(String createStmt) throws SQLException;

    protected abstract ResultSet getOneResultSet(V value) throws SQLException;

    protected abstract ResultSet getAllResultSet(V template) throws SQLException;

    protected abstract ResultSet getNResultSet(int nStart, int number, V template) throws SQLException;

    @Override
    public void create(V value) throws SQLException {
        executeUpdateStmt(value.getSqlStmtSource().getCreateStmt(value));
    }

    @Override
    public V readOne(V value) throws SQLException {
        ResultSet rs = getOneResultSet(value);
        Mappable<V> rowMapper = value.getRowMapper();
        return rowMapper.rowMap(rs);
    }

    @Override
    public List<V> readN(int nStart, int number, V template) throws SQLException {
        ResultSet rs = getNResultSet(nStart, number, template);
        return getList(rs, template);
    }

    @Override
    public List<V> readAll(V template) throws SQLException {
        ResultSet rs = getAllResultSet(template);
        return getList(rs, template);
    }

    private List<V> getList(ResultSet rs, V template) throws SQLException {
        List<V> result = new ArrayList();
        while (rs.next()) {
            Mappable<V> rowMapper = template.getRowMapper();
            result.add(rowMapper.rowMap(rs));
        }
        return result;
    }

    @Override
    public void update(V value) throws SQLException {
        executeUpdateStmt(value.getSqlStmtSource().getUpdateStmt(value));
    }

    @Override
    public void delete(V value) throws SQLException {
        executeUpdateStmt(value.getSqlStmtSource().getDeleteStmt(value));
    }

    @Override
    public void deleteAll(V template) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(V template) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
