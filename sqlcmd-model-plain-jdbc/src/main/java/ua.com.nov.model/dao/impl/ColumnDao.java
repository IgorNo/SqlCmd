package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.column.ColumnID;
import ua.com.nov.model.entity.database.DataType;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.entity.table.TableID;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ColumnDao extends DataDefinitionDao<ColumnID, Column, Table> {

    public static final String CREATE_COLUMN_SQL = "ALTER TABLE %s ADD COLUMN %s";
    private static String getColumnDefinition(Column col) {
        StringBuilder result = new StringBuilder(col.getName());
        result.append(' ').append(col.getDataType().getTypeName()).append(' ');
        return result.toString();
    }

    @Override
    public Column readOne(ColumnID pk) throws SQLException {
        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getColumns(pk.getTableID().getCatalog(), pk.getTableID().getSchema(),
                pk.getTableID().getName(), pk.getName());

        if (!rs.next()) throw new IllegalArgumentException(String.format("Column '%s' doesn't exist in table '%s.%s'.",
                pk.getName(), pk.getTableID().getSchema(), pk.getTableID().getName()));

        return  getColumn(conn, rs);
    }

    private static Column getColumn(Connection conn, ResultSet rs) throws SQLException {
        TableID tableID = TableDao.readTablePK(conn, rs);
        ColumnID columnID = new ColumnID(tableID, rs.getString("COLUMN_NAME"));
        DataType dataType = tableID.getDb().getDataType(rs.getString("TYPE_NAME"));
        Column column = new Column(rs.getInt("ORDINAL_POSITION"), columnID, dataType);
        column.setColumnSize(rs.getInt("COLUMN_SIZE"));
        column.setPrecision(rs.getInt("DECIMAL_DIGITS"));
        column.setNullable(rs.getInt("NULLABLE"));
        column.setRemarks(rs.getString("REMARKS"));
        column.setDefaultValue(rs.getString("COLUMN_DEF"));
        column.setAutoIncrement(rs.getString("IS_AUTOINCREMENT").equalsIgnoreCase("YES"));
        column.setGeneratedColumn(rs.getString("IS_GENERATEDCOLUMN").equalsIgnoreCase("YES"));
        return column;
    }

    @Override
    public Map<ColumnID, Column> readAll(Table table) throws SQLException {
        Map<ColumnID, Column> columns = new HashMap<>();

        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getColumns(table.getId().getCatalog(), table.getId().getSchema(),
                table.getId().getName(), null);

        while (rs.next()) {
            Column column = getColumn(conn, rs);
            columns.put(column.getPk(), column);
        }

        return columns;
    }

}

