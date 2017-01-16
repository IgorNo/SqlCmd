package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.column.ColumnID;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.entity.table.TableID;
import ua.com.nov.model.util.DataSourceUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TableDao extends AbstractDao<TableID, Table, Database> {

    @Override
    public void create(Table table) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(table.getPk().getDb().getExecutor().getCreateTableStmt(table));
        stmt.close();
    }


    @Override
    public Table readByPK(TableID tableID) throws SQLException {
        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(tableID.getCatalog(), tableID.getSchema(), tableID.getName(), null);

        if (!rs.next())
            throw new IllegalArgumentException(String.format("Table '%s.%s' doesn't exist",
                    tableID.getSchema(), tableID.getName()));

        return new Table(readTablePK(conn, rs), rs.getString("TABLE_TYPE"));
    }

    public static TableID readTablePK(Connection conn, ResultSet rs) throws SQLException {
        TableID pk = new TableID(DataSourceUtil.getDatabase(conn),
                rs.getString("TABLE_CAT"), rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME"));
        return pk;
    }

    @Override
    public Map<TableID, Table> readAll() throws SQLException {
        Map<TableID, Table> tables = new HashMap<>();
        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();

        ResultSet rs = dbMetaData.getTables(null, null, null, new String[] {"TABLE"});
        while (rs.next()) {
            TableID pk = readTablePK(conn, rs);
            Table table = new Table(pk, rs.getString("TABLE_TYPE"));
            tables.put(pk, table);
        }

        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnCount = rsMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            TableID tableID = new TableID(DataSourceUtil.getDatabase(conn),
                    rsMetaData.getCatalogName(i), rsMetaData.getSchemaName(i), rsMetaData.getTableName(i));
            ColumnID columnID = new ColumnID(tableID, rsMetaData.getColumnName(i));
//            Column column = new Column(columnID);

        }

        return tables;
    }

    @Override
    public void update(Table table) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(String.format(table.getPk().getDb().getExecutor().getUpdateTableStmt(table),
                table.getPk().getName(), table.getName()));
        statement.close();

    }

    @Override
    public void delete(Table table) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(String.format(table.getPk().getDb().getExecutor().getDropTableStmt(table),
                table.getPk().getName()));
        statement.close();
    }

    /**
     * Delete all tables from the current connecting database
     *
     * @param nullObject not used (only for compability to interface Dao)
     */
    @Override
    public void deleteAllFrom(Database nullObject) throws SQLException {
        Map<TableID, Table> tables = readAll();
        Statement statement = getDataSource().getConnection().createStatement();
        for (Table table : tables.values()) {
            statement.executeUpdate(String.format(table.getPk().getDb().getExecutor().getDropTableStmt(table),
                    table.getPk().getName()));
        }
        statement.close();
    }
}
