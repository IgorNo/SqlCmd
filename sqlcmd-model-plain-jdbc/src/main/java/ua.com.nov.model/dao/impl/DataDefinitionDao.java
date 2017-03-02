package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Child;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DataDefinitionDao<K extends Persistent & Child<C>, V extends Unique<K>, C>
        extends AbstractDao<K,V,C>
{
    @Override
    protected void executeUpdateStmt(String sqlStmt) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(sqlStmt);
        stmt.close();
    }

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
}
