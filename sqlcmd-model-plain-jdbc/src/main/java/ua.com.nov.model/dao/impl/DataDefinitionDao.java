package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.BaseDao;
import ua.com.nov.model.entity.Persistent;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DataDefinitionDao<K, V extends Persistent, O>
        extends BaseDao<K, V, O>
{
    private DataSource dataSource;

    protected DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(V value) throws SQLException {
        executeUpdateStmt(value.getSqlStmtSource().getCreateStmt(value));
    }

    private void executeUpdateStmt(String sqlStmt) throws SQLException {
        Statement stmt = dataSource.getConnection().createStatement();
        stmt.executeUpdate(sqlStmt);
        stmt.close();
    }

    @Override
    public void update(V value) throws SQLException {
        executeUpdateStmt(value.getSqlStmtSource().getUpdateStmt(value));
    }

    @Override
    public void delete(V value) throws SQLException {
        executeUpdateStmt(value.getSqlStmtSource().getDeleteStmt(value));
    }
}
