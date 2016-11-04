package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.*;
import ua.com.nov.model.util.DataSourceUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TableDao extends AbstractDao<TablePK, Table, Database> {

    public static final String CREATE_TABLE_SQL = "CREATE TABLE %s (%s)";
    public static final String DROP_TABLE_SQL = "DROP TABLE ";
    public static final String RENAME_TABLE_SQL = "ALTER TABLE %s RENAME TO %s";

    @Override
    public void create(Table table) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(String.format(CREATE_TABLE_SQL, table.getName(), getCreateDefinition(table)));
        statement.close();
    }

    private String getCreateDefinition(Table table) {
        String result = "";
        return result;
    }

    @Override
    public Table readByPK(TablePK tablePK) throws SQLException {
        DatabaseMetaData databaseMetaData = getDataSource().getConnection().getMetaData();
        ResultSet rs = databaseMetaData.getTables(tablePK.getSchema(), tablePK.getSchema(), tablePK.getName(), null);
        return null;
    }

    @Override
    public Map<TablePK, Table> readAll() throws SQLException {
        Map<TablePK, Table> tables = new HashMap<>();
        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();

        ResultSet rs = dbMetaData.getTables(null, null, null, new String[] {"TABLE"});
        while (rs.next()) {
            TablePK pk = new TablePK(DataSourceUtil.getDatabase(conn),
                    rs.getString("TABLE_CAT"), rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME"));
            Table table = new Table(pk, rs.getString("TABLE_TYPE"));
            tables.put(pk, table);

        }

        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnCount = rsMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            TablePK tablePK = new TablePK(DataSourceUtil.getDatabase(conn),
                    rsMetaData.getCatalogName(i), rsMetaData.getSchemaName(i), rsMetaData.getTableName(i));
            ColumnPK columnPK = new ColumnPK(tablePK, rsMetaData.getColumnName(i));
            Column column = new Column(columnPK);

        }

        return null;
    }

    @Override
    public void update(Table table) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(String.format(RENAME_TABLE_SQL, table.getPk().getName(), table.getName()));
        statement.close();

    }

    @Override
    public void delete(Table table) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(DROP_TABLE_SQL + table.getName());
        statement.close();
    }

    /**
     * Delete all tables from the current connecting database
     *
     * @param nullObject not used (only for compability to interface Dao)
     */
    @Override
    public void deleteAllFrom(Database nullObject) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(DROP_TABLE_SQL + "*");
        statement.close();
    }
}
