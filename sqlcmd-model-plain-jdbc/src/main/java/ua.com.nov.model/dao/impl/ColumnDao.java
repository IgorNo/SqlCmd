package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.column.ColumnId;
import ua.com.nov.model.entity.database.DataType;
import ua.com.nov.model.entity.table.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ColumnDao extends DataDefinitionDao<Column> {
    @Override
    protected ResultSet getOneResultSet(Column value) throws SQLException {
        return null;
    }

    @Override
    protected ResultSet getAllResultSet(Column template) throws SQLException {
        return null;
    }

    @Override
    protected ResultSet getNResultSet(int nStart, int number, Column template) throws SQLException {
        return null;
    }

    public static final String CREATE_COLUMN_SQL = "ALTER TABLE %s ADD COLUMN %s";

    private static String getColumnDefinition(Column col) {
        StringBuilder result = new StringBuilder(col.getName());
        result.append(' ').append(col.getDataType().getTypeName()).append(' ');
        return result.toString();
    }


    public Column readOne(ColumnId key) throws SQLException {
        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getColumns(key.getTable().getId().getDb().getName(), key.getTable().getId().getSchema(),
                key.getTable().getName(), key.getName());

        if (!rs.next()) throw new IllegalArgumentException(String.format("Column '%s' doesn't exist in table '%s.%s'.",
                key.getName(), key.getTable().getId().getSchema(), key.getTable().getName()));

        return  getColumn(key, rs);
    }

    private static Column getColumn(ColumnId key, ResultSet rs) throws SQLException {
        ColumnId columnId = new ColumnId(key.getTable(), rs.getString("COLUMN_NAME"));
        DataType dataType = key.getTable().getId().getDb().getDataType(rs.getString("TYPE_NAME"));
        Column column = new Column(rs.getInt("ORDINAL_POSITION"), columnId, dataType);
        column.setColumnSize(rs.getInt("COLUMN_SIZE"));
        column.setPrecision(rs.getInt("DECIMAL_DIGITS"));
        column.setNullable(rs.getInt("NULLABLE"));
        column.setRemarks(rs.getString("REMARKS"));
        column.setDefaultValue(rs.getString("COLUMN_DEF"));
        column.setAutoIncrement(rs.getString("IS_AUTOINCREMENT").equalsIgnoreCase("YES"));
        column.setGeneratedColumn(rs.getString("IS_GENERATEDCOLUMN").equalsIgnoreCase("YES"));
        return column;
    }

    public Map<ColumnId, Column> readAll(Table table) throws SQLException {
        Map<ColumnId, Column> columns = new HashMap<>();

        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getColumns(table.getId().getDb().getName(), table.getId().getSchema(),
                table.getId().getName(), null);

        while (rs.next()) {
            Column column = getColumn(new ColumnId(table, rs.getString("COLUMN_NAME")), rs);
            columns.put(column.getId(), column);
        }

        return columns;
    }

}

