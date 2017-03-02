package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.database.DataType;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.table.metadata.column.Column;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ColumnDao extends DataDefinitionDao<MetaDataId, Column, TableId> {

    @Override
    protected ResultSet getOneResultSet(MetaDataId id) throws SQLException {
        return null;
    }

    @Override
    protected ResultSet getAllResultSet(TableId template) throws SQLException {
        return null;
    }

    @Override
    protected ResultSet getNResultSet(int nStart, int number, TableId template) throws SQLException {
        return null;
    }

    public static final String CREATE_COLUMN_SQL = "ALTER TABLE %s ADD COLUMN %s";

    private static String getColumnDefinition(Column col) {
        StringBuilder result = new StringBuilder(col.getName());
        result.append(' ').append(col.getDataType().getTypeName()).append(' ');
        return result.toString();
    }


    public Column readOne(MetaDataId key) throws SQLException {
        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getColumns(key.getContainerId().getDb().getName(), key.getContainerId().getSchema(),
                key.getContainerId().getName(), key.getName());

        if (!rs.next()) throw new IllegalArgumentException(String.format("Column '%s' doesn't exist in table '%s.%s'.",
                key.getName(), key.getContainerId().getSchema(), key.getContainerId().getName()));

        return  getColumn(key, rs);
    }

    private static Column getColumn(MetaDataId key, ResultSet rs) throws SQLException {
        MetaDataId columnId = new MetaDataId(key.getContainerId(), rs.getString("COLUMN_NAME"));
        DataType dataType = key.getContainerId().getDb().getDataType(rs.getString("TYPE_NAME"));

        Column column = new Column.Builder(columnId, dataType)
        .columnSize(rs.getInt("COLUMN_SIZE")).precision(rs.getInt("DECIMAL_DIGITS"))
        .nullable(rs.getInt("TYPE_NULLABLE")).remarks(rs.getString("REMARKS"))
        .defaultValue(rs.getString("COLUMN_DEF"))
                .autoIncrement(rs.getString("IS_AUTOINCREMENT").equalsIgnoreCase("YES"))
        .generatedColumn(rs.getString("IS_GENERATEDCOLUMN").equalsIgnoreCase("YES"))
                .build();
        return column;
    }

    @Override
    public List<Column> readAll(TableId id) throws SQLException {
        List<Column> columns = new LinkedList<>();

        Connection conn = getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getColumns(id.getDb().getName(), id.getSchema(),
                id.getName(), null);

        while (rs.next()) {
            Column column = getColumn(new MetaDataId(id, rs.getString("COLUMN_NAME")), rs);
            columns.add(column);
        }

        return columns;
    }

    @Override
    protected Column rowMap(TableId containerId, ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected SqlStatementSource<MetaDataId, Column, TableId> getSqlStmtSource(Database db) {
        return null;
    }
}

