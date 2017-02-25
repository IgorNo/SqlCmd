package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.util.DbUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableDao extends DataDefinitionDao<Table> {
    @Override
    protected ResultSet getOneResultSet(Table table) throws SQLException {
        DatabaseMetaData dbMetaData = getDataSource().getConnection().getMetaData();
        ResultSet rs = dbMetaData.getTables(table.getDb().convert(table.getCatalog()),
                table.getDb().convert(table.getSchema()), table.getDb().convert(table.getName()), null);
        if (!rs.next())
            throw new IllegalArgumentException(String.format("Table '%s' doesn't exist", table.getFullName()));
        return rs;
    }

    @Override
    protected ResultSet getNResultSet(int nStart, int number, Table template) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getAllResultSet(Table table) throws SQLException {
        Connection conn = getDataSource().getConnection();
        if (DbUtil.getDatabaseUrl(conn).equalsIgnoreCase(table.getId().getDb().getDbUrl())) {
            throw new IllegalArgumentException("Connected and read databases don't equal");
        }
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(null, null, null, new String[] {"TABLE"});
        return rs;
    }
}
