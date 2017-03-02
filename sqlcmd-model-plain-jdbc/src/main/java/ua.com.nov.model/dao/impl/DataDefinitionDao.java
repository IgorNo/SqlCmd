package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.AbstractMetaDataId;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DataDefinitionDao<K extends AbstractMetaDataId<C>, V extends Unique<K>, C extends Persistent>
        extends AbstractDao<K,V,C>
{
    @Override
    public void deleteAll(C container) throws SQLException {
        List<V> vList = readAll(container);
        for (V v : vList) {
            delete(v.getId());
        }
    }

    @Override
    public int count(C container) throws SQLException {
        return readAll(container).size();
    }

    @Override
    protected void executeUpdateStmt(String sqlStmt) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(sqlStmt);
        stmt.close();
    }

    @Override
    protected ResultSet getResultSet(int nStart, int number, C containerId) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet checkResultSet(ResultSet rs, K id) throws SQLException {
        if (!rs.next()) throw new IllegalArgumentException(String.format("%s '%s' doesn't exist in %s '%s'.",
                id.getClass().getSimpleName(), id.getName(),
                id.getContainerId().getFullName(), id.getContainerId().getClass().getSimpleName()));
        return rs;
    }


    protected DatabaseMetaData getDbMetaData() throws SQLException {
        return getDataSource().getConnection().getMetaData();
    }
}
