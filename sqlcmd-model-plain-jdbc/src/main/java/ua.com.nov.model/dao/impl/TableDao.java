package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.column.ColumnPK;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.entity.table.TablePK;
import ua.com.nov.model.util.DataSourceUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TableDao extends AbstractDao<TablePK, Table, Database> {

    @Override
    public void create(Table table) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(String.format(table.getPk().getDb().getExecutor().getCreateTableStmt(),
                table.getName(), getCreateDefinition(table)));
        stmt.close();
    }

    private String getCreateDefinition(Table table) {
        String result = "";
        return result;
    }

    @Override
    public Table readByPK(TablePK tablePK) throws SQLException {
        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(tablePK.getCatalog(), tablePK.getSchema(), tablePK.getName(), null);

        if (!rs.next())
            throw new IllegalArgumentException(String.format("Table '%s.%s' doesn't exist",
                    tablePK.getSchema(), tablePK.getName()));

        return new Table(readTablePK(conn, rs), rs.getString("TABLE_TYPE"));
    }

    public static TablePK readTablePK(Connection conn, ResultSet rs) throws SQLException {
        TablePK pk = new TablePK(DataSourceUtil.getDatabase(conn),
                rs.getString("TABLE_CAT"), rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME"));
        return pk;
    }

    @Override
    public Map<TablePK, Table> readAll() throws SQLException {
        Map<TablePK, Table> tables = new HashMap<>();
        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();

        ResultSet rs = dbMetaData.getTables(null, null, null, new String[] {"TABLE"});
        while (rs.next()) {
            TablePK pk = readTablePK(conn, rs);
            Table table = new Table(pk, rs.getString("TABLE_TYPE"));
            tables.put(pk, table);
        }

        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnCount = rsMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            TablePK tablePK = new TablePK(DataSourceUtil.getDatabase(conn),
                    rsMetaData.getCatalogName(i), rsMetaData.getSchemaName(i), rsMetaData.getTableName(i));
            ColumnPK columnPK = new ColumnPK(tablePK, rsMetaData.getColumnName(i));
//            Column column = new Column(columnPK);

        }

        return tables;
    }

    @Override
    public void update(Table table) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(String.format(table.getPk().getDb().getExecutor().getUpdateTableStmt(),
                table.getPk().getName(), table.getName()));
        statement.close();

    }

    @Override
    public void delete(Table table) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(String.format(table.getPk().getDb().getExecutor().getDropTableStmt(),
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
        Map<TablePK, Table> tables = readAll();
        Statement statement = getDataSource().getConnection().createStatement();
        for (Table table : tables.values()) {
            statement.executeUpdate(String.format(table.getPk().getDb().getExecutor().getDropTableStmt(),
                    table.getPk().getName()));
        }
        statement.close();
    }
}
