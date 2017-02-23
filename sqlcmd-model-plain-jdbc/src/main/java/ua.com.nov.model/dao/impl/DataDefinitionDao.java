package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Persistent;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DataDefinitionDao<V extends Persistent>
        extends AbstractDao<V>
{
    @Override
    protected void executeUpdateStmt(String sqlStmt) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(sqlStmt);
        stmt.close();
    }

    @Override
    public void deleteAll(V template) throws SQLException {
        List<V> vList = readAll(template);
        Statement stmt = getDataSource().getConnection().createStatement();
        for (V v : vList) {
            delete(v);
        }
        stmt.close();
    }
}
