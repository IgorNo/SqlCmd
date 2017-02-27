package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.entity.table.TableId;
import ua.com.nov.model.util.DbUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableDao extends DataDefinitionDao<TableId, Table> {

    @Override
    public ResultSet getOneResultSet(TableId id) throws SQLException {
        DatabaseMetaData dbMetaData = getDataSource().getConnection().getMetaData();
        ResultSet rs = dbMetaData.getTables(id.getDb().convert(id.getCatalog()),
                id.getDb().convert(id.getSchema()), id.getDb().convert(id.getName()),
                new String[] {"TABLE"});
        if (!rs.next())
            throw new IllegalArgumentException(String.format("Table '%s' doesn't exist", id.getFullName()));
        return rs;
    }

    @Override
    protected ResultSet getNResultSet(int nStart, int number, TableId template) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getAllResultSet(TableId id) throws SQLException {
        Connection conn = getDataSource().getConnection();
        if (DbUtil.getDatabaseUrl(conn).equalsIgnoreCase(id.getDb().getDbUrl())) {
            throw new IllegalArgumentException("Connected and read databases don't equal");
        }
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(null, null, null, new String[] {"TABLE"});
        return rs;
    }
}
