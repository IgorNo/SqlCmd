package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnDao extends DataDefinitionDao<TableMdId, Column, TableId> {

    @Override
    protected ResultSet getResultSet(TableMdId id) throws SQLException {
        ResultSet rs = getDbMetaData().getColumns(id.getContainerId().getDb().getName(), id.getContainerId().getSchema(),
                id.getContainerId().getName(), id.getName());
        return rs;
    }

    @Override
    protected ResultSet getResultSet(TableId id) throws SQLException {
        return getDbMetaData().getColumns(id.getDb().getName(), id.getSchema(), id.getName(), null);
    }

    @Override
    protected ResultSet getResultSet(int nStart, int number, TableId id) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public static final String CREATE_COLUMN_SQL = "ALTER TABLE %s ADD COLUMN %s";

    private static String getColumnDefinition(Column col) {
        StringBuilder result = new StringBuilder(col.getName());
        result.append(' ').append(col.getDataType().getTypeName()).append(' ');
        return result.toString();
    }


    @Override
    public Column rowMap(TableId key, ResultSet rs) throws SQLException {
        DataType dataType = key.getContainerId().getDb().getDataType(rs.getString("TYPE_NAME"));

        Column column = new Column.Builder(key, rs.getString("COLUMN_NAME"), dataType)
                .size(rs.getInt("COLUMN_SIZE")).precision(rs.getInt("DECIMAL_DIGITS"))
                .nullable(rs.getInt("NULL")).remarks(rs.getString("REMARKS"))
                .defaultValue(rs.getString("COLUMN_DEF"))
                .autoIncrement(rs.getString("IS_AUTOINCREMENT").equalsIgnoreCase("YES"))
                .generatedColumn(rs.getString("IS_GENERATEDCOLUMN").equalsIgnoreCase("YES"))
                .build();

        return column;
    }

    @Override
    protected SqlStatementSource<TableMdId, Column, TableId> getSqlStmtSource(Database db) {
        return db.getColumnSqlStmtSource();
    }
}

