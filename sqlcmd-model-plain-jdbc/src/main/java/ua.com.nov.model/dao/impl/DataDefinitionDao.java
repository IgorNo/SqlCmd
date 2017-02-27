package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DataDefinitionDao<K extends Persistent<V>, V extends Unique>
        extends AbstractDao<K,V>
{
    @Override
    protected void executeUpdateStmt(String sqlStmt) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(sqlStmt);
        stmt.close();
    }

    @Override
    public void deleteAll(K template) throws SQLException {
        List<V> vList = readAll(template);
        for (V v : vList) {
            delete(v.getId());
        }
    }
}
