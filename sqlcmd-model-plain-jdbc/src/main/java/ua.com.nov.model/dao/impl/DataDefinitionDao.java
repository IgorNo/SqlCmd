package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.statement.SqlStatement;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DataDefinitionDao<K extends MetaDataId<C>, V extends Unique<K>, C extends Persistent>
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
    protected void executeUpdateStmt(SqlStatement sqlStmt) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(sqlStmt.getSql());
        stmt.close();
    }
}
